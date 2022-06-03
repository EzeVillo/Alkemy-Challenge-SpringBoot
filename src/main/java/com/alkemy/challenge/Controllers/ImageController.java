package com.alkemy.challenge.Controllers;

import javax.servlet.http.HttpServletRequest;

import com.alkemy.challenge.Error.Exceptions.ImageException;
import com.alkemy.challenge.Services.Interfaces.ImageService;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("image/")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping(value = "{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName, HttpServletRequest request) {

        String contentType = null;
        Resource file = null;
        try {
            file = imageService.loadImageAsResource(imageName);

            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (Exception ex) {
            throw new ImageException("The requested image could not be loaded");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
