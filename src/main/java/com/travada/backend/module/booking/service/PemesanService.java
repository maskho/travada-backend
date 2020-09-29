package com.travada.backend.module.booking.service;

import com.travada.backend.module.booking.model.Pemesan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface PemesanService {
    Pemesan createPemesan(Long idPemesan, Pemesan pemesan, MultipartFile ktp, MultipartFile paspor);
    Pemesan createPemesanBase64(Long idPemesan, Pemesan pemesan, String ktp, String paspor);

    Pemesan updatePemesan(Long id, Pemesan pemesan, MultipartFile ktp, MultipartFile paspor);

    List<Pemesan> getPemesan(Long idPemesanan);
}
