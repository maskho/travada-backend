package com.travada.backend.module.booking.repository;

import com.travada.backend.module.booking.model.Pemesan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PemesanRepository extends JpaRepository<Pemesan, Long> {
    Optional<Pemesan> findById(Long id);
    List<Pemesan> findAllByPemesananId(Long pemesananId);
}
