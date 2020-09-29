package com.travada.backend.module.booking.model.DTO;

import com.travada.backend.module.booking.model.Pemesanan;
import lombok.Data;

@Data
public class PemesananDTO {
    private Long id_user;
    private String nama_user;
    private Long id_destinasi;
    private String judul_trip;
    private Long id_pemesanan;
    private Pemesanan pemesanan;
}
