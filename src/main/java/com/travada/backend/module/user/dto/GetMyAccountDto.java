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
public class GetMyAccountDto {

    private Long id;

    private String namaLengkap;

    private Long balance;

    private String noRekening;

    private String pin;

    private String email;

    private boolean isActive;

    private boolean isAccepted;

}
