package com.babatunde.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public static final String IMAGE_DIR_UPLOAD = "photos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        showDirectory(IMAGE_DIR_UPLOAD, registry);
    }

    private void showDirectory(String imageDirUpload, ResourceHandlerRegistry registry) {
        Path path = Paths.get(imageDirUpload);
        registry.addResourceHandler("/" + imageDirUpload + "/**").addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }
 }
