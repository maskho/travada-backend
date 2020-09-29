package com.travada.backend.module.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travada.backend.module.trip.model.Destinasi;
import com.travada.backend.module.user.model.User;
import com.travada.backend.utils.AuditModel;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="pemesanan")
@Data
public class Pemesanan extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orang;
    private Long total;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinasi")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Destinasi destinasi;
}
