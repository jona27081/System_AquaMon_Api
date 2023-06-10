/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.controllers;

import java.util.Optional;
import org.aquamon.api.dto.Persons;
import org.aquamon.api.repositorys.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rules.LoginRequest;

/**
 *
 * @author zS20006736
 */
@RestController
@RequestMapping("/aquamon/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    PersonsRepository personRepository;

    public LoginController(PersonsRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    public ResponseEntity<Persons> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Persons> personOptional = personRepository.findByUsernameAndPassword(username, password);
        if (personOptional.isPresent()) {
            // Inicio de sesión exitoso
            Persons person = personOptional.get();
            return ResponseEntity.ok(person);
        } else {
            // Credenciales incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
