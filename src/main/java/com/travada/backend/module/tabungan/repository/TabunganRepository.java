package com.travada.backend.module.tabungan.repository;

import com.travada.backend.module.tabungan.model.Tabungan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabunganRepository extends JpaRepository<Tabungan, Long> {
}
