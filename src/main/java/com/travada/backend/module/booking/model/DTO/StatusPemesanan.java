package com.travada.backend.module.booking.model.DTO;

import com.travada.backend.module.booking.model.Pemesanan;
import lombok.Data;

import java.util.List;

@Data
public class StatusPemesanan {
    private int total;
    private List<Pemesanan> pemesananList;
}
