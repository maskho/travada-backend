package com.travada.backend.module.user.controller;

import com.travada.backend.module.user.dto.*;
import com.travada.backend.module.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    //Create New User
    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@ModelAttribute CreateUserDto createUser,
                                           @RequestParam(value = "foto_ktp") MultipartFile foto_ktp,
                                           @RequestParam(value = "selfie_ktp") MultipartFile selfie_ktp) {

        return userService.createNewUser(createUser, foto_ktp, selfie_ktp);

    }

    @PostMapping("/register/check")
    public ResponseEntity<?> checkRegistration(@RequestBody CheckRegisDto checkRegisDto) {

        return userService.checkRegistration(checkRegisDto);
    }

    //Confirm New User
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmNewUser(@RequestBody ConfirmationDto confirmUser) {

        return userService.confirmNewUser(confirmUser);
    }

    //Resend Code
    @PostMapping("/resend")
    public ResponseEntity<?> resendCode(@RequestBody ResendCodeDto resendCodeDto) {

        return userService.resendCode(resendCodeDto);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {

        return userService.authenticateUser(loginDto);
    }
}