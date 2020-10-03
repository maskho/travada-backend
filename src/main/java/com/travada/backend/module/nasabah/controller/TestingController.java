package com.travada.backend.module.nasabah.controller;

import com.travada.backend.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {

    @Autowired
    private BankService bankService;

    //Get All test
    @GetMapping("/testing")
    public ResponseEntity<?> getTestingResponse() {

        try {

            String accountNumber = "085868794013";
            String accountPin = "794013";
            String balance = bankService.getBalance(accountNumber, accountPin);

            return ResponseEntity.ok(balance);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
