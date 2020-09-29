package com.travada.backend.module.booking.service;

import com.travada.backend.exception.DataNotFoundException;
import com.travada.backend.module.booking.model.Cicilan;
import com.travada.backend.module.booking.repository.CicilanRepository;
import com.travada.backend.module.booking.repository.PemesananRepository;
import com.travada.backend.module.trip.model.Destinasi;
import com.travada.backend.module.trip.repository.DestinasiRepository;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service("cicilanServiceImpl")
public class CicilanServiceImpl implements CicilanService {
    @Autowired
    private PemesananRepository pemesananRepository;

    @Autowired
    private DestinasiRepository destinasiRepository;

    @Autowired
    private CicilanRepository cicilanRepository;

    @Override
    public List<Cicilan> viewCicilan(Long idDestinasi, int orang) {
        Destinasi destinasi = destinasiRepository.findById(idDestinasi)
                .orElseThrow(() -> new DataNotFoundException(idDestinasi));
        List<Cicilan> cicilanList = new ArrayList<>();
        Period periode = Period.between(destinasi.getBerangkat(), LocalDate.now());
        LocalDate jatuhTempo = LocalDate.now().plusMonths(1);

        while (jatuhTempo.isBefore(destinasi.getBerangkat())) {
            Cicilan cicilan = new Cicilan();
            cicilan.setJatuh_tempo(jatuhTempo);
            cicilan.setJumlah((destinasi.getHarga_satuan()*orang) / (periode.getMonths() + (periode.getYears() * 12)));
            cicilan.setStatus("menunggu pembayaran");
            cicilanList.add(cicilan);
            jatuhTempo = jatuhTempo.plusMonths(1);
        }
        return cicilanList;
    }

    @Override
    public List<Cicilan> createCicilan(Long idDestinasi, Long idPemesanan, int orang) {
        List<Cicilan> cicilanList = viewCicilan(idDestinasi, orang);

        for(Cicilan cicilan: cicilanList){
            pemesananRepository.findById(idPemesanan).map(pemesanan -> {
                cicilan.setPemesanan(pemesanan);
                return cicilanRepository.save(cicilan);
            }).orElseThrow(()->new DataNotFoundException(idPemesanan));
        }
        return cicilanList;
    }

    @Override
    public List<Cicilan> getCicilan(Long idPemesanan) {
        return cicilanRepository.findAllByPemesananId(idPemesanan);
    }

    @Override
    public Cicilan updateCicilanById(Long id, String status) {
        Cicilan cicilan = cicilanRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));
        cicilan.setStatus(status);
        cicilanRepository.save(cicilan);
        return cicilan;
    }

    @Override
    public BaseResponse findCicilanById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> dropById(Long id) {
        return null;
    }


}
