package com.itgate.ProShift.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User projectMaster;

    @JsonIgnore // Ignore during serialization
    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    private List<User> employees;

    @JsonIgnore // Ignore during serialization
    @OneToMany(mappedBy = "equipe" ,cascade = CascadeType.ALL)
    private List<Projet> projects;

    @PrePersist
    private void prePersist() {
        dateCreation = new Date();
    }

    // Getter for employees
    @JsonIgnore // Ignore during serialization
    public List<User> getEmployees() {
        return employees;
    }

    // Setter for employees for deserialization
    @JsonProperty("employees") // Specify the property name from the JSON
    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    // Getter for projects
    @JsonIgnore // Ignore during serialization
    public List<Projet> getProjects() {
        return projects;
    }

    // Setter for projects for deserialization
    @JsonProperty("projects") // Specify the property name from the JSON
    public void setProjects(List<Projet> projects) {
        this.projects = projects;
    }
}

