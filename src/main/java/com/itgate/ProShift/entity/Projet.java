package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projet {

    public enum Etat{TERMINER,ENCOURS,ANNULEE}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Date dateFinEstimee;
    private Date dateFin;
    private Date dateDebut;
    private String description;
    private Etat etat;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User chef;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Tache> taches;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }


}
