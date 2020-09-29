package com.travada.backend.module.trip.service;

import com.cloudinary.utils.ObjectUtils;
import com.travada.backend.config.CloudinaryConfig;
import com.travada.backend.exception.DataNotFoundException;
import com.travada.backend.module.trip.model.Destinasi;
import com.travada.backend.module.trip.repository.DestinasiRepository;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("destinasiServiceImpl")
public class DestinasiServiceImpl implements DestinasiService {
    @Autowired
    private DestinasiRepository destinasiRepository;

    @Autowired
    private CloudinaryConfig cloudc;

    @Transactional
    public BaseResponse saveDestinasi(Destinasi destinasi, MultipartFile[] foto) {
        BaseResponse baseResponse = new BaseResponse();
        List<String> gambar = new ArrayList<>();
        if (foto != null) {
            for (MultipartFile file : foto) {
                try {
                    Map uploadResult = cloudc.upload(file.getBytes(),
                            ObjectUtils.asMap("resourcetype", "auto"));
                    gambar.add(uploadResult.get("url").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        destinasi.setGambar_list(gambar);
        destinasi.setPopularitas();
        destinasi.setDurasi(destinasi.getBerangkat(), destinasi.getPulang());
        destinasiRepository.save(destinasi);

        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(destinasi);
        baseResponse.setMessage("Pembuatan destinasi trip telah berhasil");

        return baseResponse;
    }

    @Transactional
    public BaseResponse findAll() {
        BaseResponse baseResponse = new BaseResponse();
        List<Destinasi> destinasiList = destinasiRepository.findAll();

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasiList);
        baseResponse.setMessage("Pengambilan list data destinasi telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findAllSortByPopularitas() {
        BaseResponse baseResponse = new BaseResponse();
        List<Destinasi> destinasiList = destinasiRepository.findAll(Sort.by("popularitas").descending());

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasiList);
        baseResponse.setMessage("Pengambilan list data destinasi berdasarkan popularitas telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findAllSortByPilihan() {
        BaseResponse baseResponse = new BaseResponse();
        List<Destinasi> destinasiList = destinasiRepository.findAll(Sort.by("kapasitas").descending());

        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasiList);
        baseResponse.setMessage("Pengambilan list data destinasi berdasarkan pilihan telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findAllFilterHarga(Long termurah, Long termahal, String[] benuaList) {
        BaseResponse baseResponse = new BaseResponse();
        List<Destinasi> destinasiList = new ArrayList<>();
        for (String benua : benuaList) {
            destinasiList.addAll(destinasiRepository.findAllByFilterHarga_Satuan(benua, termurah, termahal));
        }


        if (destinasiList == null) {
            baseResponse.setStatus(HttpStatus.NO_CONTENT);
            baseResponse.setData(null);
            baseResponse.setMessage("Tidak ada data tersedia");
            return baseResponse;
        }
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasiList);
        baseResponse.setMessage("Pengambilan list data destinasi berdasarkan filter telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findAllBySearch(String keyword) {
        BaseResponse baseResponse = new BaseResponse();
        List<Destinasi> destinasiList = destinasiRepository.search(keyword);

        if (destinasiList == null) {
            baseResponse.setStatus(HttpStatus.NO_CONTENT);
            baseResponse.setData(null);
            baseResponse.setMessage("Tidak ada data tersedia");
            return baseResponse;
        }
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasiList);
        baseResponse.setMessage("Pengambilan list data destinasi berdasarkan pencarian telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse findById(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        Destinasi destinasi = destinasiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));
        destinasi.setPopularitas();
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setData(destinasi);
        baseResponse.setMessage("Pengambilan data dengan id " + id + " telah berhasil");
        return baseResponse;
    }

    @Transactional
    public BaseResponse editById(Long id, Destinasi newDestinasi, MultipartFile[] foto) {
        BaseResponse baseResponse = new BaseResponse();

        Destinasi destinasi = destinasiRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(id)
                );
        if (newDestinasi.getNama_trip() != null) {
            destinasi.setNama_trip(newDestinasi.getNama_trip());
        }
        if (newDestinasi.getBenua() != null) {
            destinasi.setBenua(newDestinasi.getBenua());
        }
        if (newDestinasi.getLokal() != null) {
            destinasi.setLokal(newDestinasi.getLokal());
        }
        if (foto != null) {
            List<String> gambar = new ArrayList<>();
            for (MultipartFile file : foto) {
                try {
                    Map uploadResult = cloudc.upload(file.getBytes(),
                            ObjectUtils.asMap("resourcetype", "auto"));
                    gambar.add(uploadResult.get("url").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            destinasi.setGambar_list(gambar);
        }
        if (newDestinasi.getKapasitas() != 0) {
            destinasi.setKapasitas(newDestinasi.getKapasitas());

        }
        if (newDestinasi.getHarga_satuan() != null) {
            destinasi.setHarga_satuan(newDestinasi.getHarga_satuan());

        }
        if (newDestinasi.getOverview() != null) {
            destinasi.setOverview(newDestinasi.getOverview());

        }
        if (newDestinasi.getDeskripsi() != null) {
            destinasi.setDeskripsi(newDestinasi.getDeskripsi());

        }
        if (newDestinasi.getBerangkat() != null && newDestinasi.getPulang() != null) {
            destinasi.setBerangkat(newDestinasi.getBerangkat());
            destinasi.setPulang(newDestinasi.getPulang());
            destinasi.setDurasi(newDestinasi.getBerangkat(), newDestinasi.getPulang());

        }
        if (newDestinasi.getRencana_list() != null) {
            destinasi.setRencana_list(newDestinasi.getRencana_list());

        }
        if (newDestinasi.getFasilitas() != null) {
            destinasi.setFasilitas(newDestinasi.getFasilitas());

        }
        if (newDestinasi.getInfo_waktu_cuaca() != null) {
            destinasi.setInfo_waktu_cuaca(newDestinasi.getInfo_waktu_cuaca());

        }
        if (newDestinasi.getInfo_persiapan() != null) {
            destinasi.setInfo_persiapan(newDestinasi.getInfo_persiapan());
        }
        if (newDestinasi.getSyarat_ketentuan() != null) {
            destinasi.setSyarat_ketentuan(newDestinasi.getSyarat_ketentuan());
        }
        if (newDestinasi.getKapasitas_terisi() != 0) {
            destinasi.setKapasitas_terisi(newDestinasi.getKapasitas_terisi());
        }

        destinasiRepository.save(destinasi);

        baseResponse.setStatus(HttpStatus.CREATED);
        baseResponse.setData(destinasi);
        baseResponse.setMessage("Data dengan id " + destinasi.getId() + " telah diupdate");
        return baseResponse;
    }

    @Transactional
    public ResponseEntity<?> dropDestinasi(Long id) {
        return destinasiRepository.findById(id)
                .map(destinasi -> {
                    destinasiRepository.delete(destinasi);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new DataNotFoundException(id));
    }

}
