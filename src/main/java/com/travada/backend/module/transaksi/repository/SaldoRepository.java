package com.travada.backend.module.transaksi.repository;

import com.travada.backend.module.transaksi.model.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    Saldo findByUserId(Long userId);
}
