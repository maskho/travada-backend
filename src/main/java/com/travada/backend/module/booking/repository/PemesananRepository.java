package com.travada.backend.module.booking.repository;

import com.travada.backend.module.booking.model.Pemesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PemesananRepository extends JpaRepository<Pemesanan, Long> {
    List<Pemesanan> findAllByDestinasiId(Long idDestinasi);
    List<Pemesanan> findAllByUserId(Long idUser);

    Optional<Pemesanan> findByDestinasiIdAndUserId(Long idDestinasi, Long idUser);
}
