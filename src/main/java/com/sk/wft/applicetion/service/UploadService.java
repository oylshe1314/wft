package com.sk.wft.applicetion.service;

import com.sk.wft.applicetion.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Paths;

@Service
public class UploadService {

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

    public Mono<ResponseDto<?>> save(String dir, Flux<FilePart> uploadsFlux) {
        String savePath = StringUtils.hasText(dir) ? this.uploadLocation + (dir.endsWith("/") ? dir : dir + "/") : this.uploadLocation;
        return uploadsFlux.doFirst(() -> {
                    if (savePath.equals(this.uploadLocation)) {
                        return;
                    }

                    Paths.get(savePath);
                    File file = new File(savePath);
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            throw new RuntimeException(String.format("Create directory '%s' failed.", dir));
                        }
                    } else if (!file.isDirectory()) {
                        throw new RuntimeException(String.format("Exists a file with the same name as '%s'.", dir));
                    }
                })
                .flatMap(uploadFile -> uploadFile.transferTo(new File(savePath + uploadFile.filename())))
                .collectList()
                .map(voids -> ResponseDto.success());
    }
}
