package com.travada.backend.module.booking.service;

import com.travada.backend.module.booking.model.Cicilan;
import com.travada.backend.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface CicilanService {
    List<Cicilan> viewCicilan(Long idDestinasi, int orang);

    List<Cicilan> createCicilan(Long idDestinasi, Long idPemesanan, int orang);

    List<Cicilan> getCicilan(Long idPemesanan);

    Cicilan updateCicilanById(Long id, String status);


    BaseResponse findCicilanById(Long id);

    ResponseEntity<?> dropById(Long id);
}
