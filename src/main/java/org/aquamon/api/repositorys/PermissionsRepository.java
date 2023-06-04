/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.repositorys;

import org.aquamon.api.dto.DTOPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author zS20006736
 */
public interface PermissionsRepository extends JpaRepository<DTOPermission, String>{
    
}
