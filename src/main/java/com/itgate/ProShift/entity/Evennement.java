package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Evennement {
    public enum Type{SPORT,LOISIR,GAMING,FORMATION,TEAMBUILDING}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Enumerated(EnumType.STRING)
    private Type type;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date datetime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinateur_id")
    private User coordinateur;

    @OneToMany(mappedBy = "evennement", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Invitation> invitations;


    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

}
