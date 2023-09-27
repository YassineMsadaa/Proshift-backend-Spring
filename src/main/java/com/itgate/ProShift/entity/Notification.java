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
public class Notification {
    public enum Type{DEMANDE,NOTE,EVENNEMENT,INVITATION,TACHE}
    public enum Retour{ACCEPTEE,REFUSEE,TERMINEE,ANNULEE}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    private Type type;
    private Retour retour;
    private boolean vu;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

}

