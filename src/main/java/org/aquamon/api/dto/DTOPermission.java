/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.dto;

import java.util.Random;
import javax.persistence.*;
import lombok.*;

/**
 *
 * @author zS20006736
 */
@Data
@Entity
@Table(name="permissions")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DTOPermission {
    @Id
    @Column(name = "id_permission")
    private String id;
    
    @Column(name = "permission")
    private String permission;
    
    public DTOPermission(String st){
        this.permission = st;
        generateId();
    }
    
    @PrePersist
    public void generateId() {
        String nombre = this.getPermission();
        String nombreCorto = nombre.substring(0, Math.min(nombre.length(), 3));
        String numerosAleatorios = String.format("%03d", new Random().nextInt(1000));
        this.id = nombreCorto.toUpperCase() + numerosAleatorios;
    }
}


