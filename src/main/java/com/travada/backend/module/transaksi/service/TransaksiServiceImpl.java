package com.travada.backend.module.transaksi.service;

import com.travada.backend.config.security.UserPrincipal;
import com.travada.backend.module.transaksi.dto.TopupSaldoDto;
import com.travada.backend.module.transaksi.dto.TransferDto;
import com.travada.backend.module.transaksi.model.Saldo;
import com.travada.backend.module.transaksi.repository.SaldoRepository;
import com.travada.backend.module.user.model.User;
import com.travada.backend.module.user.repository.UserRepository;
import com.travada.backend.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransaksiServiceImpl implements TransaksiService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    @Override
    public ResponseEntity<?> topUpSaldo(UserPrincipal userPrincipal, TopupSaldoDto topupSaldoDto) {

        try {
            User currentUser = userRepository.findById(userPrincipal.getId())
                    .orElseThrow();

            Saldo existSaldo = saldoRepository.findByUserId(userPrincipal.getId());
            System.out.println(existSaldo);
            System.out.println(currentUser);

            if (existSaldo == null) {

                Saldo newSaldo = new Saldo();
                newSaldo.setSaldo(topupSaldoDto.getAmount());
                newSaldo.setUser(currentUser);
                saldoRepository.save(newSaldo);

            } else {
                Long beforeSaldo = existSaldo.getSaldo();
                existSaldo.setSaldo(beforeSaldo + topupSaldoDto.getAmount());
                saldoRepository.save(existSaldo);
            }

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            topupSaldoDto,
                            "succes"),
                    HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            e.getMessage(),
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<?> transferSaldo(UserPrincipal userPrincipal, TransferDto transferDto) {
        try {
            User currentUser = userRepository.findById(userPrincipal.getId())
                    .orElseThrow();
            User targetUser = userRepository.findBynoRekening(transferDto.getDestination());

            if (targetUser == null) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.UNPROCESSABLE_ENTITY,
                                null,
                                "No rekening tidak ditemukan"),
                        HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Saldo senderSaldo = saldoRepository.findByUserId(userPrincipal.getId());
            Saldo targetSaldo = saldoRepository.findByUserId(targetUser.getId());

            //Origin saldo sender
            Long originSaldo = senderSaldo.getSaldo();
            if (transferDto.getAmount() >= originSaldo) {
                return new ResponseEntity(new BaseResponse
                        (HttpStatus.BAD_REQUEST,
                                transferDto,
                                "Saldo tidak cukup"),
                        HttpStatus.BAD_REQUEST);
            }
            senderSaldo.setSaldo(originSaldo - transferDto.getAmount());

            if (targetSaldo == null) {
                Saldo newSaldo = new Saldo();
                newSaldo.setSaldo(transferDto.getAmount());
                newSaldo.setUser(targetUser);

                saldoRepository.save(senderSaldo);
                saldoRepository.save(newSaldo);
            } else {
                Long beforeSaldo = targetSaldo.getSaldo();
                targetSaldo.setSaldo(beforeSaldo + transferDto.getAmount());
                saldoRepository.save(targetSaldo);
                saldoRepository.save(senderSaldo);
            }

            return new ResponseEntity(new BaseResponse
                    (HttpStatus.OK,
                            transferDto,
                            "succes"),
                    HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new BaseResponse
                    (HttpStatus.BAD_GATEWAY,
                            null,
                            "Server error"),
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
