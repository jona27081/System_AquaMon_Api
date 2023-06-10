/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.aquamon.api.dto.DTOAdmin;
import org.aquamon.api.dto.DTOEmployee;
import org.aquamon.api.dto.DTOLog;
import org.aquamon.api.dto.DTOWaterPump;
import org.aquamon.api.repositorys.AdminsRepository;
import org.aquamon.api.repositorys.EmployeesRepository;
import org.aquamon.api.repositorys.LogsRepository;
import org.aquamon.api.repositorys.WaterPumpsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rules.AccessCodes;
import rules.AccessResult;
import rules.DTOAdminAccessChecker;
import rules.DTOEmployeeAccessChecker;

/**
 *
 * @author zS20006736
 Agregar validacion para cuando no exista lo bomba que se le esta pasando al metodo getAllLogsByWaterPumps
 
 
 */
@RestController
@RequestMapping("/aquamon/logs")
@CrossOrigin(origins = "*")
public class LogsController {

    @Autowired
    AdminsRepository aRepository;
    @Autowired
    EmployeesRepository eRepository;
    @Autowired
    WaterPumpsRepository wRepository;
    @Autowired
    LogsRepository lRepository;

    public boolean checkAccess(List<String> ids, String idPass) {
        return ids.contains(idPass);
    }

    public AccessResult getEntityByIdForAccess(String id, String codeAccess) {
        Optional<DTOEmployee> employeeData = eRepository.findById(id);
        if (employeeData.isPresent()) {
            return new AccessResult(employeeData.get(), new DTOEmployeeAccessChecker(), codeAccess);
        }

        Optional<DTOAdmin> adminData = aRepository.findById(id);
        if (adminData.isPresent()) {
            return new AccessResult(adminData.get(), new DTOAdminAccessChecker(), codeAccess);
        }

        return null;
    }

    @GetMapping("/{idAccess}/{idpump}")
    public ResponseEntity<List<DTOLog>> getAllLogsByWaterPumps(@PathVariable("idAccess") String idAccess, @PathVariable("idpump") String idPump) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CONSULTAR_LOGS_POR_BOMBA"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOLog> ltsLogs = lRepository.findByidpump(idPump);                
                return new ResponseEntity<>(ltsLogs, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/{idpump}")
    public ResponseEntity<DTOLog> createWaterPump(@PathVariable("idpump") String idPump, @RequestBody DTOLog log) {
        Optional<DTOWaterPump> waterPump = wRepository.findById(idPump);
        if(waterPump.isPresent()){
            try {
                log.setIdpump(waterPump.get().getId());
                DTOLog _adm = lRepository.save(new DTOLog(
                        log.getIdpump(),
                        log.getLevelwater(),
                        log.getPumpstate()
                ));
                return new ResponseEntity<>(_adm, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


}
