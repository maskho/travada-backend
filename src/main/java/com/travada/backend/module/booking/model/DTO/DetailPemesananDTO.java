package com.travada.backend.module.booking.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travada.backend.module.booking.model.Cicilan;
import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.model.Pemesanan;
import com.travada.backend.module.trip.model.Destinasi;
import lombok.Data;

import java.util.List;

@Data
public class DetailPemesananDTO {
    private Pemesanan pemesanan;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destinasi destinasi;
    private List<Cicilan> cicilan;
    private List<Pemesan> pemesan;
}
