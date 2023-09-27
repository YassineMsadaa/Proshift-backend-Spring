package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    public enum TypeDemande{Conge,Teletravail,Autorisation}
    public enum StatusDemande{En_attante,Acceptee,Refusee}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeDemande type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private int nombreJours;
    @Enumerated(EnumType.STRING)
    private StatusDemande status;
    @ManyToOne
    @JoinColumn (name = "employee_id")
    @JsonIgnoreProperties("user")
    private User user;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

    }
