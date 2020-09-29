package com.travada.backend.module.nasabah.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListUserDto {

    private Long id;

    private String nama_lengkap;

    private String username;

    private String email;

    private String no_hp;

    private String no_rekening;

    private String fotoKtp;

    private String selfieKtp;

    private String no_ktp;

    private Date tgl_lahir;

    private String jenis_kelamin;

    private boolean isActive;

    private Object tags;

}
