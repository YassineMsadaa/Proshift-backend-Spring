package com.itgate.ProShift.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    public enum EtatCivil{CELIBATAIRE,MARIE,DIVORCE}
    public enum Poste{TECHNICIEN,INGENIEUR}
    public enum Departement{RH,WEBDEV,MOBDEV,FINANCE}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String  cin;
    private String  resetPasswordToken;
    private String  verificationCode ;
    private boolean blocked;
    private String  matricule;
    private String  nom;
    private String  prenom;
    private Date    dateNaissance;
    private Integer numTelephone;
    private String  adresse;
    private Date    dateEmbauche;
    private String  urgenceNom;
    private Integer urgenceNum;
    private Integer congeSolde;
    private Date    checkin;
    private boolean approved;
    private boolean statut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date        dateCreation;
    @Enumerated(EnumType.STRING)
    private Poste       poste;
    @Enumerated(EnumType.STRING)
    private EtatCivil   etatCivil;
    @Enumerated(EnumType.STRING)
    private Departement departement;
    @ManyToOne
    @JoinColumn(name = "equipe_id")
    @JsonIgnore
    private Equipe equipe;

    @OneToMany(mappedBy = "coordinateur", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Evennement> evennements;

    @OneToMany(mappedBy = "coordinateur",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Note> notes;

    @OneToMany(mappedBy = "invitee",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Invitation> receivedInvitations;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Presence> presences;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
