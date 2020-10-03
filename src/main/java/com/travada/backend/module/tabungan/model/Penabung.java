//package com.travada.backend.module.tabungan.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import javax.persistence.*;
//
//public class Penabung {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String nama_penabung;
//    private String nomor_rekening;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_tabungan")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private Tabungan tabungan;
//
//
//    public void setTabungan(Tabungan tabungan) {
//        this.tabungan = tabungan;
//    }
//
//    public Tabungan getTabungan() {
//        return tabungan;
//    }
//}
