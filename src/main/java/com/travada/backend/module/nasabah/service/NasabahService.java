package com.travada.backend.module.nasabah.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface NasabahService {

    public ResponseEntity<?> getAllUser(String status);

    public ResponseEntity<?> getDetailUser(Long id);

    public ResponseEntity<?> acceptUserAccount(Long id, String status);

    public ResponseEntity<?> getNewUserNotif();
}
