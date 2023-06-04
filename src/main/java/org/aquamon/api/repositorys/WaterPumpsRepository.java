/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package org.aquamon.api.repositorys;

import java.util.List;
import org.aquamon.api.dto.DTOWaterPump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author zS20006736
 */
public interface WaterPumpsRepository extends JpaRepository<DTOWaterPump, String> {
    List<DTOWaterPump> findByAssignment(String assignment);
}
