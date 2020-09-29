package com.travada.backend.module.trip.controller;

import com.travada.backend.module.trip.model.Destinasi;
import com.travada.backend.module.trip.service.DestinasiService;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/destinasi")
public class DestinasiController {
    @Autowired
    private DestinasiService destinasiService;

    @GetMapping("/all")
    public BaseResponse getAll() {
        return destinasiService.findAll();
    }

    @GetMapping("/populer")
    public BaseResponse getPopuler() {
        return destinasiService.findAllSortByPopularitas();
    }

    @GetMapping("/pilihan")
    public BaseResponse getPilihan() {
        return destinasiService.findAllSortByPilihan();
    }

    @GetMapping("/hargabenua")
    public BaseResponse getRangeHarga(@RequestParam Long termurah, @RequestParam Long termahal, @RequestParam String[] benua) {
        return destinasiService.findAllFilterHarga(termurah, termahal, benua);
    }

    @GetMapping("/pencarian")
    public BaseResponse getPencarian(@RequestParam String keyword) {
        if (keyword != "") {
            return destinasiService.findAllBySearch(keyword);
        }
        return destinasiService.findAll();
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id) {
        return destinasiService.findById(id);
    }


    @PostMapping()
    public BaseResponse createDestinasi(@ModelAttribute Destinasi destinasi,
                                        @RequestParam MultipartFile[] foto) {
        return destinasiService.saveDestinasi(destinasi, foto);
    }

    @PutMapping("/{id}")
    public BaseResponse putById(@PathVariable Long id, @ModelAttribute Destinasi destinasiReq, @RequestParam MultipartFile[] foto) {
        return destinasiService.editById(id, destinasiReq, foto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropDestinasi(@PathVariable Long id) {
        return destinasiService.dropDestinasi(id);
    }
}
