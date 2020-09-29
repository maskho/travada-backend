package com.travada.backend.module.trip.model;

import com.travada.backend.utils.AuditModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "Destinasi")
@Table(name = "destinasi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Destinasi extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_trip")
    private String nama_trip;
    @Column(name = "benua")
    private String benua;
    private Boolean lokal;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "gambar_trip", joinColumns = @JoinColumn(name = "id_trip"))
    private List<String> gambar_list;
    private int kapasitas;
    @Column(name = "harga_satuan")
    private Long harga_satuan;
    @Column(name = "overview")
    private String overview;
    @Column(name = "deskripsi")
    private String deskripsi;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate berangkat;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pulang;
//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "hari", joinColumns = @JoinColumn(name = "id_trip"))
//    private List<String> hari;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rencana_list", joinColumns = @JoinColumn(name = "id_trip"))
    private List<String> rencana_list;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "fasilitas_trip", joinColumns = @JoinColumn(name = "id_trip"))
    private Set<String> fasilitas;
    private String info_waktu_cuaca;
    private String info_persiapan;
    private String syarat_ketentuan;
    @Setter(AccessLevel.NONE)
    private int durasi;
    @Setter(AccessLevel.NONE)
    private int popularitas;
    @Column(name = "kapasitas_terisi")
    private int kapasitas_terisi;


    public void setDurasi(LocalDate berangkat, LocalDate pulang) {
        this.durasi = Period.between(berangkat, pulang).getDays();
    }

    public void setPopularitas() {
        this.popularitas++;
    }
}
