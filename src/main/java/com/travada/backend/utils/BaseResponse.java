package com.travada.backend.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Setter @Getter
@NoArgsConstructor
public class BaseResponse {
    private HttpStatus status;
    private String message;
    private Object data;

    public BaseResponse(HttpStatus status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
