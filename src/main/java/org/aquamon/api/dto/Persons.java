/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.dto;

import java.util.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author zS20006736
 */
@Data
@Entity
@NoArgsConstructor()
public class Persons {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "active")
    private String active;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "lastmodification")
    private Date lastmodification;
    @Column(name = "modifiedby")
    private String modifiedby;

    public Persons(String name, String lastname, String phone, String active, String modifiedby) {
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.active = active;
        this.modifiedby = modifiedby;
        InstFecha();
    }
    
    public void InstFecha(){
        Date fecha = Date.from(Instant.now());
        this.setLastmodification(new java.sql.Date(fecha.getTime()));
    }
}
