/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.dto;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author zS20006736
 */
@Data
@Entity
@NoArgsConstructor()
@Table(name = "waterpumps")
public class DTOWaterPump {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "location")
    private String location;
    @Column(name = "enabled")
    private String enabled;
    @Column(name = "assignment")
    private String assignment;
    @Column(name = "lastmodification")
    private Date lastmodification;
    @Column(name = "modifiedby")
    private String modifiedby;

    public DTOWaterPump(String name, String location, String enabled, String assignment, String modifiedby) {
        generateId();
        this.name = name;
        this.location = location;
        this.enabled = enabled;
        this.assignment = assignment;
        this.modifiedby = modifiedby;
    }

    @PrePersist()
    public void generateId() {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        StringBuilder resultado = new StringBuilder();

        // Generar 3 letras aleatorias
        resultado.append(letras.charAt(random.nextInt(letras.length())));
        resultado.append(letras.charAt(random.nextInt(letras.length())));
        resultado.append(letras.charAt(random.nextInt(letras.length())));

        // Generar 4 números aleatorios
        String randomNumbers = String.format("%03d", random.nextInt(1000));
        resultado.append(randomNumbers);
        
        this.id = resultado.toString();
        
        Date fecha = Date.from(Instant.now());
        this.setLastmodification(new java.sql.Date(fecha.getTime()));
        
    }
}
