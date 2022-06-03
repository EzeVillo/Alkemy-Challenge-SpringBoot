package com.alkemy.challenge.Services.Interfaces;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public void init();

    public void deleteAll();
    
    public String createImage(MultipartFile image, String nombre);

    public Resource loadImageAsResource(String nombre);

    public Path loadImage(String nombre);

    public void deleteImage(String nombre);

    public String copyImageWithNewName(String newName, String imageName);

    public String getUri(String imageName);

    public String getImageNameFromUri(String uri);
}
