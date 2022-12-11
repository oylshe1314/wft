package com.sk.wft.applicetion.dto;

import lombok.Getter;

public record RequestDto<T>(int sessionId, String caller, T data) {

}
