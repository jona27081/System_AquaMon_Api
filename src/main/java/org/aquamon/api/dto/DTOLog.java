/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.dto;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author zS20006736
 */

@Data
@Entity
@Table(name="logs")
@NoArgsConstructor()
public class DTOLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "idpump")
    private String idpump;
    @Column(name = "levelwater")
    private int levelwater;
    @Column(name = "pumpstate")
    private String pumpstate;
    @Column(name = "logdatetime")
    private LocalDateTime logDateTime;

    public DTOLog(String idpump, int levelwater, String pumpstate) {
        this.idpump = idpump;
        this.levelwater = levelwater;
        this.pumpstate = pumpstate;
        this.logDateTime = LocalDateTime.now();
    }   
    
}
