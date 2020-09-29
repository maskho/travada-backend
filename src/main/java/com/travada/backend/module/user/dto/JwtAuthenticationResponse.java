package com.travada.backend.module.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JwtAuthenticationResponse {
    private String token;
    private String tokenType = "Bearer";
    private String pin;

    public JwtAuthenticationResponse(String token,String pin) {
        this.token = token;
        this.pin = pin;
    }
}
