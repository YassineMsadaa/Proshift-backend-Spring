package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    public enum Status{ACCEPTE,REFUSE,DEFAULT}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Enumerated(EnumType.STRING)
    private Status status=Status.DEFAULT;
    @ManyToOne
    @JoinColumn(name = "evennement_id")
    @JsonIgnore
    private Evennement evennement;

    @ManyToOne
    @JoinColumn(name = "coordinateur_id")
    @JsonIgnore
    private User coordinateur;

    @ManyToOne
    @JoinColumn(name = "invitee_id")

    private User invitee;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

}

