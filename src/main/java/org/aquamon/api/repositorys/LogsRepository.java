/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package org.aquamon.api.repositorys;

import java.util.List;
import org.aquamon.api.dto.DTOLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author zS20006736
 */
public interface LogsRepository extends JpaRepository<DTOLog, Long> {
    List<DTOLog> findByidpump(String idpump);
}
