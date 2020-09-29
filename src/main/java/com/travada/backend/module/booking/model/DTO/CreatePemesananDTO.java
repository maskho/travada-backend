package com.travada.backend.module.booking.model.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Data
public class CreatePemesananDTO {
    private Long IdDestinasi;

    //pemesananRepository
    private int orang;

    //pemesanRepository
    private List<String> nama;
    private List<String> no_hp;
    private List<String> email;
    private List<String> ktp;
    private List<String> paspor;

    //cicilanRepository
}
