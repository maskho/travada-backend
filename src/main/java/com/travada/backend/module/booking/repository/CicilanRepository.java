package com.travada.backend.module.booking.repository;

import com.travada.backend.module.booking.model.Cicilan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CicilanRepository extends JpaRepository<Cicilan, Long> {
    List<Cicilan> findAllByPemesananId(Long pemesananId);
    Optional<Cicilan> findByIdAndPemesananId(Long id, Long pemesananId);
}
