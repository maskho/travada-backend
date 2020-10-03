package com.travada.backend.module.tabungan.service;

import com.cloudinary.utils.ObjectUtils;
import com.travada.backend.config.CloudinaryConfig;
import com.travada.backend.exception.DataNotFoundException;
import com.travada.backend.module.tabungan.model.Tabungan;
//import com.travada.backend.module.tabungan.repository.PenabungRepository;
import com.travada.backend.module.tabungan.repository.TabunganRepository;
import com.travada.backend.module.user.repository.UserRepository;
import com.travada.backend.utils.BaseResponse;
import com.travada.backend.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("tabunganServiceImpl")
public class TabunganServiceImpl implements TabunganService {
    @Autowired
    private TabunganRepository tabunganRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PenabungRepository penabungRepository;

    @Autowired
    private CloudinaryConfig cloudinaryConfig;



    @Transactional
    public BaseResponse saveTabungan(Tabungan tabungan,
                                     MultipartFile gambar_tabungan) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Map uploadResult = cloudinaryConfig.upload(gambar_tabungan.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            tabungan.setGambar_tabungan(uploadResult.get("url").toString());

        } catch (IOException e) {
            return new BaseResponse
                    (HttpStatus.BAD_REQUEST,
                            null,
                            "Upload foto gagal");
        }
        Period periode = Period.between(tabungan.getTarget(), LocalDate.now());
        if (tabungan.getPeriode().toLowerCase() == "harian") {
            tabungan.setJumlah_setoran((tabungan.getJumlah_tabungan() - (tabungan.getSetoran_awal() * tabungan.getJumlah_orang())) /
                    periode.getDays() * tabungan.getJumlah_orang());
        }
        else if (tabungan.getPeriode().toLowerCase() == "mingguan") {
            tabungan.setJumlah_setoran((tabungan.getJumlah_tabungan() - (tabungan.getSetoran_awal() * tabungan.getJumlah_orang())) /
                    (periode.getDays() / 7) * tabungan.getJumlah_orang());
        }
        else if (tabungan.getPeriode().toLowerCase() == "bulanan") {
            tabungan.setJumlah_setoran((tabungan.getJumlah_tabungan() - (tabungan.getSetoran_awal() * tabungan.getJumlah_orang())) /
                    (periode.getMonths() * tabungan.getJumlah_orang()));
        }
        tabunganRepository.save(tabungan);
        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(tabungan);
        baseResponse.setMessage("New Trava Save Success");

        return baseResponse;
    }

    @Transactional
    public BaseResponse editById(Long id, Tabungan newTabungan, MultipartFile gambar_tabungan) {
        BaseResponse baseResponse = new BaseResponse();

        Tabungan tabungan = tabunganRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(id));
        try {
            Map uploadResult = cloudinaryConfig.upload(gambar_tabungan.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            tabungan.setGambar_tabungan(uploadResult.get("url").toString());

        } catch (IOException e) {
            return new BaseResponse
                    (HttpStatus.BAD_REQUEST,
                            null,
                            "Upload foto gagal");
        }
        if (newTabungan.getTujuan() != null) {
            tabungan.setTujuan(newTabungan.getTujuan());
        }
        if (newTabungan.getJumlah_tabungan() != null) {
            tabungan.setJumlah_tabungan(newTabungan.getJumlah_tabungan());
        }
        if (newTabungan.getTarget() != null) {
            tabungan.setTarget(newTabungan.getTarget());
        }

        tabunganRepository.save(tabungan);

        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(tabungan);
        baseResponse.setMessage("Data with id : " + tabungan.getId() + "is updated");
        return baseResponse;
    }

    @Transactional
    public ResponseEntity<?> dropSave(Long id) {
        return tabunganRepository.findById(id)
                .map(save -> {
                    tabunganRepository.delete(save);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new DataNotFoundException(id));
    }

    @Transactional
    public BaseResponse findAll() {
        BaseResponse baseResponse = new BaseResponse();
        List<Tabungan> tabunganList = tabunganRepository.findAll();

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(tabunganRepository);
        baseResponse.setMessage("Success");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findById(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        Tabungan tabungan = tabunganRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));
        tabungan.setId(id);
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(tabungan);
        baseResponse.setMessage("Tabungan dengan " + id + " telah berhasil");
        return baseResponse;
    }


}
