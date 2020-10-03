package com.travada.backend.module.tabungan.service;

import com.travada.backend.module.tabungan.model.Tabungan;
import com.travada.backend.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface TabunganService {
    BaseResponse saveTabungan (Tabungan tabungan, MultipartFile gambar_tabungan);

    BaseResponse editById(Long id, Tabungan tabungan, MultipartFile gambar_tabungan);

    BaseResponse findAll();

    BaseResponse findById(Long id);

    ResponseEntity<?> dropSave(Long id);
}
