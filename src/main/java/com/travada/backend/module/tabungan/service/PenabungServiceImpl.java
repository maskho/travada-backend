//package com.travada.backend.module.tabungan.service;
//
//import com.travada.backend.exception.DataNotFoundException;
//import com.travada.backend.module.tabungan.model.Penabung;
//import com.travada.backend.module.tabungan.repository.PenabungRepository;
//import com.travada.backend.module.tabungan.repository.TabunganRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("penabungServiceImpl")
//public class PenabungServiceImpl implements PenabungService{
//
//    @Autowired
//    PenabungRepository penabungRepository;
//
//    @Autowired
//    TabunganService tabunganService;
//
//    @Autowired
//    TabunganRepository tabunganRepository;
//
//    @Override
//    public Penabung createPenabung(Long idTabungan, Penabung penabung) {
//        tabunganRepository.findById(idTabungan).map(tabungan -> {
//            penabung.setTabungan(tabungan);
//            return penabungRepository.save(penabung);
//        }).orElseThrow(() -> new DataNotFoundException(idTabungan));
//
//        return penabung;
//    }
//
//    @Override
//    public Penabung updatePenabung(Long id, Penabung penabungPayload) {
//        Penabung penabung = penabungRepository.findById(id)
//                .orElseThrow(()-> new DataNotFoundException(id));
//        penabungRepository.save(penabung);
//        return penabung;
//    }
//
//    @Override
//    public List<Penabung> getPenabung(Long idTabungan) {
//        return penabungRepository.findAllByTabunganId(idTabungan);
//    }
//
//
//}
