package com.travada.backend.module.nasabah.controller;

import com.travada.backend.module.nasabah.service.NasabahServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NasabahController {

    @Autowired
    private NasabahServiceImpl nasabahService;

    //Get All user
    @GetMapping("/nasabah")
    public ResponseEntity<?> getAllUser(
            @RequestParam(value = "status", required = false) String status) {

        return nasabahService.getAllUser(status);
    }

    //Get detail user
    @GetMapping("/nasabah/{id}")
    public ResponseEntity<?> getDetailUser(@PathVariable Long id) {

        return nasabahService.getDetailUser(id);
    }

    //Persetujuan nasabah
    @PutMapping("/nasabah/{id}")
    public ResponseEntity<?> confirmUserAccount(@PathVariable Long id,
                                                @RequestParam String status) {
        return nasabahService.acceptUserAccount(id, status);
    }

    //Get notification new user
    @GetMapping("/notifadmin")
    public ResponseEntity<?> getNewUserNotif() {
        return nasabahService.getNewUserNotif();
    }
}
