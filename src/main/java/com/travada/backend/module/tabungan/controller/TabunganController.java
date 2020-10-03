package com.travada.backend.module.tabungan.controller;

import com.travada.backend.module.tabungan.model.Tabungan;
import com.travada.backend.module.tabungan.service.TabunganService;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/tabungan")
public class TabunganController {
    @Autowired
    private TabunganService tabunganService;

    @PostMapping()
    public BaseResponse createSave(@ModelAttribute Tabungan tabungan,
                                   @RequestParam MultipartFile gambar_tabungan){
        return tabunganService.saveTabungan(tabungan, gambar_tabungan);
    }
    @GetMapping("/all")
    public BaseResponse getAll() {
        return tabunganService.findAll();
    }

    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable Long id) {
        return tabunganService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropSave(@PathVariable Long id){
        return tabunganService.dropSave(id);
    }
}
