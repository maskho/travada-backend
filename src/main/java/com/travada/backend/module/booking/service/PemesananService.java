package com.travada.backend.module.booking.service;

import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.model.Pemesanan;
import com.travada.backend.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface PemesananService {
    Pemesanan savePemesanan(Long idUser, Long idDestinasi, Pemesanan pemesanan);

    BaseResponse findAll();

    BaseResponse findByIdUser(Long idUser);

    Pemesanan findByDestinasiIdAndUserId(Long idDestinasi, Long idUser);

    Pemesanan findById(Long id);

    Pemesanan updateStatusById(Long id, String status);

    ResponseEntity<?> dropById(Long id);
}
