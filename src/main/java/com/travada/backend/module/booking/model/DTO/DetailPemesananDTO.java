package com.travada.backend.module.booking.model.DTO;

import com.travada.backend.module.booking.model.Cicilan;
import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.model.Pemesanan;
import lombok.Data;

import java.util.List;

@Data
public class DetailPemesananDTO {
    private Pemesanan pemesanan;
    private List<Cicilan> cicilan;
    private List<Pemesan> pemesan;
}
