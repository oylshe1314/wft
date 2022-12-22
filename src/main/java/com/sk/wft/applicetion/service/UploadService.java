package com.sk.wft.applicetion.service;

import com.sk.wft.applicetion.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Paths;

@Service
public class UploadService {

    private String uploadLocation;

    @Value("${source.upload.location}")
    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
        if (!this.uploadLocation.endsWith("/")) {
            this.uploadLocation += "/";
        }
    }

    private String createDir(String dir) throws RuntimeException {
        String savePath = this.uploadLocation + (dir.endsWith("/") ? dir : dir + "/");
        Paths.get(savePath);
        File file = new File(savePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException(String.format("Create directory '%s' failed.", dir));
            }
        } else if (!file.isDirectory()) {
            throw new RuntimeException(String.format("Exists a file with the same name as '%s'.", dir));
        }
        return savePath;
    }

    public Mono<ResponseDto<?>> save(Mono<String> dirMono, Flux<FilePart> uploadsFlux) {
        return dirMono.map(this::createDir)
                .defaultIfEmpty(this.uploadLocation)
                .flatMap(savePath -> uploadsFlux.flatMap(uploadFile -> uploadFile.transferTo(new File(savePath + uploadFile.filename())))
                        .collectList()
                        .map(voids -> ResponseDto.success())
                );
    }
}
