package com.travada.backend.module.transaksi.service;

import com.travada.backend.config.security.UserPrincipal;
import com.travada.backend.module.transaksi.dto.TopupSaldoDto;
import com.travada.backend.module.transaksi.dto.TransferDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface TransaksiService {

    public ResponseEntity<?> topUpSaldo(UserPrincipal userPrincipal, TopupSaldoDto topupSaldoDto);

    public ResponseEntity<?> transferSaldo(UserPrincipal userPrincipal, TransferDto transferDto);
}
