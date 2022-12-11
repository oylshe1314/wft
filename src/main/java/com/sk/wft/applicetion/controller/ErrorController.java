package com.sk.wft.applicetion.controller;

import com.sk.wft.applicetion.dto.ResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = {Exception.class})
    public Mono<ResponseDto<?>> error(Exception exception) {
        return Mono.just(ResponseDto.fail(500, exception.getMessage()));
    }
}
