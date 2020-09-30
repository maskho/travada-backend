package com.travada.backend.module.tabungan.repository;

import com.travada.backend.module.tabungan.model.Penabung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PenabungRepository extends JpaRepository<Penabung, Long> {
    Optional<Penabung> findById(Long id);

    List<Penabung> findAllByTabunganId(Long tabunganId);
}
