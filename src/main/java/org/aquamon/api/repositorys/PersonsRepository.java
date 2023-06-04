/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package org.aquamon.api.repositorys;

import java.util.Optional;
import org.aquamon.api.dto.Persons;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author zS20006736
 */
public interface PersonsRepository extends JpaRepository<Persons, String> {
     Optional<Persons> findByUsernameAndPassword(String username, String password);
}
