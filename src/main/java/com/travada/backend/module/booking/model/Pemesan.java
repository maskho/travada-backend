package com.travada.backend.module.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travada.backend.utils.AuditModel;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "pemesan")
@Data
public class Pemesan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nama;
    private String no_hp;
    private String email;
    private String ktp;
    private String paspor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pemesanan")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Pemesanan pemesanan;
}
