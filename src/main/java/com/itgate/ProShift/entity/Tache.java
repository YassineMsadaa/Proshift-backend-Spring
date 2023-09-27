package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tache {

    public enum Priorite{FAIBLE,
        MOYENNE_FAIBLE,
        MOYENNE,
        MOYENNE_HAUTE,
        ÉLEVÉE;}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private boolean done;
    private LocalDateTime dateFin;
    private LocalDateTime dateFinEstimee;
    private LocalDateTime DateAssignation;
    private Priorite priorite;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "Employee_id")
    private User employee;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }
}
