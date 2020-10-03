package com.travada.backend.module.transaksi.controller;

import com.travada.backend.config.security.CurrentUser;
import com.travada.backend.config.security.UserPrincipal;
import com.travada.backend.module.transaksi.dto.TopupSaldoDto;
import com.travada.backend.module.transaksi.dto.TransferDto;
import com.travada.backend.module.transaksi.service.TransaksiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class TransaksiController {

    @Autowired
    private TransaksiServiceImpl transaksiService;

    //Add saldo testing
    @PostMapping("/topup")
    public ResponseEntity<?> userTopUp(@CurrentUser UserPrincipal userPrincipal,
                                       @RequestBody TopupSaldoDto topupSaldoDto) {
        return transaksiService.topUpSaldo(userPrincipal, topupSaldoDto);
    }

    //Transfer testing
    @PostMapping("/transfer")
    public ResponseEntity<?> userTransfer(@CurrentUser UserPrincipal userPrincipal,
                                          @RequestBody TransferDto transferDto) {
        return transaksiService.transferSaldo(userPrincipal, transferDto);
    }
}
