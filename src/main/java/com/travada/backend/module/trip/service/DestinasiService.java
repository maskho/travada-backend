package com.travada.backend.module.trip.service;

import com.travada.backend.module.trip.model.Destinasi;
import com.travada.backend.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface DestinasiService {
    BaseResponse saveDestinasi(Destinasi destinasi, MultipartFile[] foto);

    BaseResponse findAll();

    BaseResponse findAllSortByPopularitas();

    BaseResponse findAllSortByPilihan();

    BaseResponse findAllFilterHarga(Long termurah, Long termahal, String[] benua);

    BaseResponse findAllBySearch(String keyword);

    BaseResponse findById(Long id);

    BaseResponse editById(Long id, Destinasi destinasi, MultipartFile[] foto);

    ResponseEntity<?> dropDestinasi(Long id);
}
