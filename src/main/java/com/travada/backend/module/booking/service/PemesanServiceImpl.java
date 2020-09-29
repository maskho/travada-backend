package com.travada.backend.module.booking.service;

import com.cloudinary.utils.ObjectUtils;
import com.travada.backend.config.CloudinaryConfig;
import com.travada.backend.exception.DataNotFoundException;
import com.travada.backend.module.booking.model.Pemesan;
import com.travada.backend.module.booking.repository.PemesanRepository;
import com.travada.backend.module.booking.repository.PemesananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service("pemesanServiceImpl")
public class PemesanServiceImpl implements PemesanService {
    @Autowired
    CloudinaryConfig cloudinary;

    @Autowired
    PemesanRepository pemesanRepository;

    @Autowired
    PemesananRepository pemesananRepository;


    @Override
    public Pemesan createPemesanBase64(Long idPemesanan, Pemesan pemesan, String ktp, String paspor) {


        byte[] ktpBytes = Base64.getMimeDecoder().decode(ktp);
        byte[] pasporBytes = Base64.getMimeDecoder().decode(paspor);

        Map uploadResult = cloudinary.upload(ktpBytes,
                ObjectUtils.asMap("resourcetype", "auto"));

        pemesan.setKtp(uploadResult.get("url").toString());

        Map uploadResult2 = cloudinary.upload(pasporBytes,
                ObjectUtils.asMap("resourcetype", "auto"));

        pemesan.setPaspor(uploadResult2.get("url").toString());


        pemesananRepository.findById(idPemesanan).map(pemesanan -> {
            pemesan.setPemesanan(pemesanan);
            return pemesanRepository.save(pemesan);
        }).orElseThrow(() -> new DataNotFoundException(idPemesanan));

        return pemesan;
    }

    @Override
    public Pemesan createPemesan(Long idPemesanan, Pemesan pemesan, MultipartFile ktp, MultipartFile paspor) {


        try {
            Map uploadResult = cloudinary.upload(ktp.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            pemesan.setKtp(uploadResult.get("url").toString());

            Map uploadResult2 = cloudinary.upload(paspor.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            pemesan.setPaspor(uploadResult2.get("url").toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
        pemesananRepository.findById(idPemesanan).map(pemesanan -> {
            pemesan.setPemesanan(pemesanan);
            return pemesanRepository.save(pemesan);
        }).orElseThrow(() -> new DataNotFoundException(idPemesanan));

        return pemesan;
    }

    @Override
    public Pemesan updatePemesan(Long id, Pemesan pemesanPayload, MultipartFile ktp, MultipartFile paspor) {
        Pemesan pemesan = pemesanRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));

        if (pemesanPayload.getNama() != null) {
            pemesan.setNama(pemesanPayload.getNama());
        }
        if (pemesanPayload.getEmail() != null) {
            pemesan.setEmail(pemesanPayload.getEmail());
        }
        if (pemesanPayload.getNo_hp() != null) {
            pemesan.setNo_hp(pemesanPayload.getNo_hp());
        }
        if (ktp != null) {
            try {
                Map uploadResult = cloudinary.upload(ktp.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                pemesan.setKtp(uploadResult.get("url").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (paspor != null) {
            try {
                Map uploadResult2 = cloudinary.upload(paspor.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                pemesan.setPaspor(uploadResult2.get("url").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pemesanRepository.save(pemesan);
        return pemesan;
    }

    @Override
    public List<Pemesan> getPemesan(Long idPemesanan) {
        return pemesanRepository.findAllByPemesananId(idPemesanan);
    }
}
