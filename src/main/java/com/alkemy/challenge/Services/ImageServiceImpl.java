package com.alkemy.challenge.Services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.alkemy.challenge.Controllers.ImageController;
import com.alkemy.challenge.Error.Exceptions.ImageException;
import com.alkemy.challenge.Error.Exceptions.ValidationException;
import com.alkemy.challenge.Services.Interfaces.ImageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${root}")
    private Path root;

    public String createImage(MultipartFile image, String name) {
        name = name.replace(" ", "");
        ValidCharacters(name);
        String extension = StringUtils.getFilenameExtension(StringUtils.cleanPath(
                image.getOriginalFilename()));
        name += "." + extension;
        try {
            if (image.isEmpty()) {
                throw new ValidationException("Can't save an empty image");
            }
            try (InputStream stream = image.getInputStream()) {
                Files.copy(stream, this.root.resolve(name),
                        StandardCopyOption.REPLACE_EXISTING);
                return name;
            }
        } catch (IOException e) {
            throw new ImageException("Image could not be saved");
        }
    }

    public Resource loadImageAsResource(String name) {
        try {
            Path archive = loadImage(name);
            Resource resource = new UrlResource(archive.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ImageException(
                        "There was an error reading the file");

            }
        } catch (MalformedURLException e) {
            throw new ImageException("There was an error reading the file");
        }
    }

    public Path loadImage(String name) {
        return root.resolve(name);
    }

    public void deleteImage(String name) {
        try {
            Path archive = loadImage(name);
            Files.deleteIfExists(archive);
        } catch (IOException e) {
            throw new ImageException("Error deleting a file");
        }

    }

    public String copyImageWithNewName(String newName, String imageName) {
        ValidCharacters(newName);
        Resource image = loadImageAsResource(imageName);
        String extension = StringUtils.getFilenameExtension(StringUtils.cleanPath(
                imageName));
        newName += "." + extension;
        try (InputStream stream = image.getInputStream()) {
            Files.copy(stream, this.root.resolve(newName),
                    StandardCopyOption.REPLACE_EXISTING);
            return newName;
        } catch (IOException e) {
            throw new ImageException("There was an error renaming the file");
        }
    }

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new ImageException("Could not initialize storage");
        }
    }

    public void deleteAll(){
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public String getUri(String imageName){
        return MvcUriComponentsBuilder
                        .fromMethodName(ImageController.class, "getImage", imageName, null)  
                        .build().toUriString();
    }

    public String getImageNameFromUri(String uri){
        return uri.split("image/")[1];
    }

    private void ValidCharacters(String name) {
        if (name.contains("/"))
            throw new ValidationException("Cannot save an image that includes the character /");
        if (name.contains("\\"))
            throw new ValidationException("Cannot save an image that includes the character \\");
        if (name.contains(":"))
            throw new ValidationException("Cannot save an image that includes the character :");
        if (name.contains("*"))
            throw new ValidationException("Cannot save an image that includes the character *");
        if (name.contains("?"))
            throw new ValidationException("Cannot save an image that includes the character ?");
        if (name.contains("\""))
            throw new ValidationException("Cannot save an image that includes the character \"");
        if (name.contains("<"))
            throw new ValidationException("Cannot save an image that includes the character <");
        if (name.contains(">"))
            throw new ValidationException("Cannot save an image that includes the character >");
        if (name.contains("|"))
            throw new ValidationException("Cannot save an image that includes the character |");
    }
}
