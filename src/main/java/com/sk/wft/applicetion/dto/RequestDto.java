package com.sk.wft.applicetion.dto;

public record RequestDto<T>(int sessionId, String caller, T data) {

}
