package com.sk.wft.applicetion.controller;

import com.sk.wft.applicetion.dto.ResponseDto;
import com.sk.wft.applicetion.service.UploadService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public Mono<ResponseDto<?>> files(@RequestPart(value = "dir", required = false) Mono<String> dirMono, @RequestPart("file") Flux<FilePart> uploadsFlux) {
        return uploadService.save(dirMono, uploadsFlux);
    }
}
