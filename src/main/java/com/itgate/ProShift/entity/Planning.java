package com.itgate.ProShift.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Collection;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String titre;
    private String type;
    private Date hdlundi;
    private Date hflundi;
    private Date hdmardi;
    private Date hfmardi;
    private Date hdmercredi;
    private Date hfmercredi;
    private Date hdjeudi;
    private Date hfjeudi;
    private Date hdvendredi;
    private Date hfvendredi;

}
