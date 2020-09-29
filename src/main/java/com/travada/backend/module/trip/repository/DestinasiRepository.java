package com.travada.backend.module.trip.repository;

import com.travada.backend.module.trip.model.Destinasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinasiRepository extends JpaRepository<Destinasi, Long> {
    @Query("select d from Destinasi d where d.benua = ?1 and d.harga_satuan between ?2 and ?3 order by d.harga_satuan asc")
    List<Destinasi> findAllByFilterHarga_Satuan(String benua, Long termurah, Long termahal);

    @Query("select d from Destinasi d where lower(concat(d.nama_trip,'', d.overview,'', d.benua)) like lower(concat('%',:keyword,'%'))")
    List<Destinasi> search(@Param("keyword") String keyword);

}
