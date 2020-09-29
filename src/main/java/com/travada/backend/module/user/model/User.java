package com.travada.backend.module.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.travada.backend.utils.AuditModel;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "nama_lengkap", nullable = false, unique = true)
    private String namaLengkap;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "no_hp")
    private String noHp;

    @Column(name = "no_rekening")
    private String noRekening;

    @Column(name = "foto_ktp")
    private String fotoKtp;

    @Column(name = "selfie_ktp")
    private String selfieKtp;

    @Column(name = "no_ktp")
    private String noKtp;

    @Column(name = "tgl_lahir")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date tglLahir;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "pin")
    private String pin;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;


    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_accepted")
    private boolean isAccepted;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
