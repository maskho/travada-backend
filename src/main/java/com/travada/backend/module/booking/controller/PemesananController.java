package com.travada.backend.module.booking.controller;

import com.travada.backend.module.booking.model.Cicilan;
import com.travada.backend.module.booking.model.DTO.CreatePemesananDTO;
import com.travada.backend.module.booking.model.DTO.DetailPemesananDTO;
import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.model.Pemesanan;
import com.travada.backend.module.booking.service.CicilanService;
import com.travada.backend.module.booking.service.PemesanService;
import com.travada.backend.module.booking.service.PemesananService;
import com.travada.backend.module.trip.repository.DestinasiRepository;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pemesanan")
public class PemesananController {
    @Autowired
    private PemesananService pemesananService;

    @Autowired
    private CicilanService cicilanService;

    @Autowired
    private PemesanService pemesanService;

    @Autowired
    private DestinasiRepository destinasiRepository;

    @GetMapping("/all")
    public BaseResponse getAll() {
        return pemesananService.findAll();
    }

    @GetMapping("/{idUser}/destinasi/{idDestinasi}")
    public BaseResponse getById(@PathVariable Long idUser, @PathVariable Long idDestinasi) {
        BaseResponse baseResponse = new BaseResponse();
        DetailPemesananDTO detailPemesananDTO = new DetailPemesananDTO();

        Pemesanan pemesanan = pemesananService.findByDestinasiIdAndUserId(idDestinasi, idUser);
        List<Pemesan> pemesanList = pemesanService.getPemesan(pemesanan.getId());
        List<Cicilan> cicilanList = cicilanService.getCicilan(pemesanan.getId());

        detailPemesananDTO.setPemesanan(pemesanan);
        detailPemesananDTO.setPemesan(pemesanList);
        detailPemesananDTO.setCicilan(cicilanList);

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(detailPemesananDTO);
        baseResponse.setMessage("pengambilan detail pemesanan dengan id user " + idUser + " dan id destinasi " + idDestinasi + " berhasil dilakukan");
        return baseResponse;
    }

    @GetMapping("/{idUser}")
    public BaseResponse getAllByIdUser(@PathVariable Long idUser) {
        return pemesananService.findByIdUser(idUser);
    }


    @PostMapping("/{idUser}")
    public BaseResponse createPemesanan(@ModelAttribute CreatePemesananDTO pemesananDTO, @PathVariable Long idUser, @RequestParam MultipartFile[] ktp, @RequestParam MultipartFile[] paspor) {
        BaseResponse baseResponse = new BaseResponse();
        DetailPemesananDTO detailPemesananDTO = new DetailPemesananDTO();
        Pemesanan pemesanan = new Pemesanan();
        List<Pemesan> pemesanList = new ArrayList<>(pemesananDTO.getOrang());


        pemesanan.setOrang(pemesananDTO.getOrang());
        pemesanan.setStatus("menunggu");
        pemesanan = pemesananService.savePemesanan(idUser, pemesananDTO.getIdDestinasi(), pemesanan);

        for (int i = 0; i < pemesananDTO.getOrang(); i++) {
            Pemesan pemesanData = new Pemesan();
            pemesanData.setNama(pemesananDTO.getNama().get(i));
            pemesanData.setNo_hp(pemesananDTO.getNo_hp().get(i));
            pemesanData.setEmail(pemesananDTO.getEmail().get(i));

            pemesanList.add(pemesanService.createPemesan(pemesanan.getId(), pemesanData, ktp[i], paspor[i]));
        }

        List<Cicilan> cicilanList = cicilanService.createCicilan(pemesananDTO.getIdDestinasi(), pemesanan.getId(), pemesananDTO.getOrang());

        detailPemesananDTO.setPemesanan(pemesanan);
        detailPemesananDTO.setCicilan(cicilanList);
        detailPemesananDTO.setPemesan(pemesanList);

        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(detailPemesananDTO);
        baseResponse.setMessage("pemesanan telah dibuat");
        return baseResponse;
    }

    @PutMapping("/{id}")
    public BaseResponse updateStatusPemesanan(@PathVariable Long id, @RequestBody String status) {
        BaseResponse baseResponse = new BaseResponse();
        Pemesanan pemesanan = pemesananService.updateStatusById(id, status);

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(pemesanan);
        baseResponse.setMessage("status pemesanan dengan id " + id + " berhasil diupdate");
        return baseResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePemesanan(@PathVariable Long id) {
        return pemesananService.dropById(id);
    }
}
