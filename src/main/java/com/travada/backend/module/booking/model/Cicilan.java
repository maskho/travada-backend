package com.travada.backend.module.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travada.backend.utils.AuditModel;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "cicilan")
@Data
public class Cicilan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jumlah;
    private LocalDate jatuh_tempo;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pemesanan")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Pemesanan pemesanan;
}
