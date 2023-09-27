package com.itgate.ProShift.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembreEntreprise {
    @Id
    private String cin;
    private String name;
    private String lastName;
    private String role;
}
