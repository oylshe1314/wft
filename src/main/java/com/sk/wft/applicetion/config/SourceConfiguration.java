package com.sk.wft.applicetion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@SpringBootApplication
public class SourceConfiguration implements WebFluxConfigurer {

    private static final String FILE_PREFIX = "file:///";

    private String uploadLocation;

    private String downloadPattern;

    @Value("${source.upload.location}")
    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
        if (!this.uploadLocation.endsWith("/")) {
            this.uploadLocation += "/";
        }
    }

    @Value("${source.download.pattern}")
    public void setDownloadPattern(String downloadPattern) {
        this.downloadPattern = downloadPattern;
        if (!this.downloadPattern.endsWith("/")) {
            this.downloadPattern += "/";
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(this.downloadPattern + "**").addResourceLocations(FILE_PREFIX + this.uploadLocation);
    }
}
