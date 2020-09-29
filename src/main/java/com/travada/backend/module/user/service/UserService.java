package com.travada.backend.module.user.service;

import com.travada.backend.module.user.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface UserService {

    public ResponseEntity<?> createNewUser(CreateUserDto createUser, MultipartFile foto_ktp, MultipartFile selfie_ktp);

    public ResponseEntity<?> confirmNewUser(ConfirmationDto confirmUser);

    public ResponseEntity<?> resendCode(ResendCodeDto resendCodeDto);

    public ResponseEntity<?> authenticateUser(LoginDto loginDto);

    public ResponseEntity<?> checkRegistration(CheckRegisDto checkRegisDto);
}
