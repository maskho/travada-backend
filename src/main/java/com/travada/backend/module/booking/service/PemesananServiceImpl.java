package com.travada.backend.module.booking.service;

import com.travada.backend.exception.DataNotFoundException;
import com.travada.backend.module.booking.model.DTO.DetailPemesananDTO;
import com.travada.backend.module.booking.model.DTO.PemesananDTO;
import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.model.Pemesanan;
import com.travada.backend.module.booking.repository.CicilanRepository;
import com.travada.backend.module.booking.repository.PemesanRepository;
import com.travada.backend.module.booking.repository.PemesananRepository;
import com.travada.backend.module.trip.repository.DestinasiRepository;
import com.travada.backend.module.user.repository.UserRepository;
import com.travada.backend.utils.BaseResponse;
import com.travada.backend.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("pemesananServiceImpl")
public class PemesananServiceImpl implements PemesananService{
    @Autowired
    private PemesananRepository pemesananRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("destinasiRepository")
    @Autowired
    private DestinasiRepository destinasiRepository;

    @Autowired
    private PemesanRepository pemesanRepository;

    @Autowired
    private CicilanRepository cicilanRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;


    @Override
    public Pemesanan savePemesanan(Long idUser, Long idDestinasi, Pemesanan pemesanan) {
        userRepository.findById(idUser).map(user -> {
            pemesanan.setUser(user);
            destinasiRepository.findById(idDestinasi).map(destinasi -> {
                destinasi.setKapasitas_terisi(pemesanan.getOrang());
                destinasiRepository.save(destinasi);
                pemesanan.setTotal(destinasi.getHarga_satuan()*pemesanan.getOrang());
                pemesanan.setDestinasi(destinasi);
                return pemesananRepository.save(pemesanan);
            }).orElseThrow(()->new DataNotFoundException(idDestinasi));
            return pemesananRepository.save(pemesanan);
        }).orElseThrow(()-> new DataNotFoundException(idUser));

        return pemesanan;
    }

    @Override
    public BaseResponse findAll() {
        BaseResponse baseResponse = new BaseResponse();
        List<Pemesanan> pemesananList = pemesananRepository.findAll();
        List<PemesananDTO> pemesananDTOS = new ArrayList<>();

        for(Pemesanan pemesanan: pemesananList){
            PemesananDTO pemesananDTO = modelMapperUtil
                    .modelMapperInit()
                    .map(pemesanan, PemesananDTO.class);
            pemesananDTO.setPemesanan(pemesanan);
            pemesananDTO.setId_destinasi(pemesanan.getDestinasi().getId());
            pemesananDTO.setId_user(pemesanan.getUser().getId());
            pemesananDTO.setNama_user(pemesanan.getUser().getUsername());
            pemesananDTO.setJudul_trip(pemesanan.getDestinasi().getNama_trip());
            pemesananDTO.setId_pemesanan(pemesanan.getId());
            pemesananDTOS.add(pemesananDTO);
        }
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(pemesananDTOS);
        baseResponse.setMessage("pengambilan list data pemesanan trip telah berhasil");
        return baseResponse;
    }

    @Override
    public BaseResponse findByIdUser(Long idUser) {
        BaseResponse baseResponse = new BaseResponse();
        List<Pemesanan> pemesananList = pemesananRepository.findAllByUserId(idUser);
        List<PemesananDTO> pemesananDTOS = new ArrayList<>();

        for(Pemesanan pemesanan: pemesananList){
            PemesananDTO pemesananDTO = modelMapperUtil
                    .modelMapperInit()
                    .map(pemesanan, PemesananDTO.class);
            pemesananDTO.setPemesanan(pemesanan);
            pemesananDTO.setId_destinasi(pemesanan.getDestinasi().getId());
            pemesananDTO.setId_user(pemesanan.getUser().getId());
            pemesananDTO.setNama_user(pemesanan.getUser().getUsername());
            pemesananDTO.setJudul_trip(pemesanan.getDestinasi().getNama_trip());
            pemesananDTO.setId_pemesanan(pemesanan.getId());
            pemesananDTOS.add(pemesananDTO);
        }
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(pemesananDTOS);
        baseResponse.setMessage("pengambilan list data pemesanan trip dengan id user "+idUser+" telah berhasil");
        return baseResponse;
    }

    @Override
    public Pemesanan findByDestinasiIdAndUserId(Long idDestinasi, Long idUser) {
        BaseResponse baseResponse = new BaseResponse();
        Pemesanan pemesanan = pemesananRepository.findByDestinasiIdAndUserId(idDestinasi,idUser)
                .orElseThrow(()->new DataNotFoundException(idDestinasi));
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(pemesanan);
        baseResponse.setMessage("Pengambilan data pemesanan dengan id destinasi " + idDestinasi+" dan id user "+idUser + " telah berhasil");
        return pemesanan;
    }

    @Override
    public Pemesanan findById(Long id) {

        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(id));

       return pemesanan;
    }

    @Override
    public Pemesanan updateStatusById(Long id, String status) {
        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(id));
        pemesanan.setStatus(status);
        return pemesananRepository.save(pemesanan);
    }

    @Override
    public ResponseEntity<?> dropById(Long id) {
        return pemesananRepository.findById(id)
                .map(pemesanan -> {
                    pemesananRepository.delete(pemesanan);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new DataNotFoundException(id));
    }
}
