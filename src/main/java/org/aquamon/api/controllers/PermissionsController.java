/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.controllers;

import org.aquamon.api.repositorys.PermissionsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aquamon.api.dto.DTOAdmin;
import org.aquamon.api.dto.DTOEmployee;
import org.aquamon.api.dto.DTOPermission;
import org.aquamon.api.dto.DTOWaterPump;
import org.aquamon.api.repositorys.AdminsRepository;
import org.aquamon.api.repositorys.EmployeesRepository;
import org.aquamon.api.repositorys.WaterPumpsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 */
@RestController
@RequestMapping("/aquamon/permissions")
public class PermissionsController {

    @Autowired
    PermissionsRepository pRepository;
    @Autowired
    AdminsRepository aRepository;
    @Autowired
    EmployeesRepository eRepository;

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

    @GetMapping("/{idAccess}/")
    public ResponseEntity<List<DTOPermission>> getAllPermissions(@PathVariable("idAccess") String idAccess) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_BOMBAS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOPermission> ltsPermissions = new ArrayList<>();
                pRepository.findAll().forEach(ltsPermissions::add);
                return new ResponseEntity<>(ltsPermissions, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/{idAccess}/")
    public ResponseEntity<DTOPermission> createPermission(@PathVariable("idAccess") String idAccess, @RequestBody DTOPermission pms) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CREAR_PERMISOS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                DTOPermission _pms = pRepository.save(new DTOPermission(pms.getPermission()));
                return new ResponseEntity<>(_pms, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
}
