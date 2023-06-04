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
import org.aquamon.api.dto.DTOPermission;
import org.aquamon.api.repositorys.AdminsRepository;
import org.aquamon.api.repositorys.EmployeesRepository;
import org.aquamon.api.repositorys.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * @author zS20006736 Tanto como en las funciones de actualizar de Employee y
 * Admins, falta implementar metodo checkIdsExist, ya que me permite actualizar
 * permisos que no existen Nota de version 0.0.1
 */
@RestController
@RequestMapping("/aquamon/admins")
public class AdminsController {

    @Autowired
    AdminsRepository aRepository;
    @Autowired
    PermissionsRepository pRepository;
    @Autowired
    EmployeesRepository eRepository;

    public boolean checkIdsExist(List<String> ids) {
        for (String id : ids) {
            Optional<DTOPermission> personOptional = pRepository.findById(id);
            if (personOptional.isEmpty()) {
                return false;
            }
        }
        return true;
    }

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

    @GetMapping("/{idAccess}/")
    public ResponseEntity<List<DTOAdmin>> getAllAdmins(@PathVariable("idAccess") String idAccess) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_ADMINS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOAdmin> ltsAdm = new ArrayList<>();
                aRepository.findAll().forEach(ltsAdm::add);
                return new ResponseEntity<>(ltsAdm, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOAdmin> getAdminById(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_ADMINS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOAdmin> perData = aRepository.findById(id);
            if (perData.isPresent()) {
                return new ResponseEntity<>(perData.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/{idAccess}/")
    public ResponseEntity<DTOAdmin> createAdmin(@PathVariable("idAccess") String idAccess, @RequestBody DTOAdmin adm) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_REGISTRAR_ADMIN"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            if (true == checkIdsExist(adm.getListPermissions())) {
                try {
                    adm.setModifiedby(idAccess);
                    DTOAdmin _adm = aRepository.save(new DTOAdmin(
                            adm.getName(),
                            adm.getLastname(),
                            adm.getPhone(),
                            adm.getActive(),
                            adm.getModifiedby(),
                            adm.getListPermissions()
                    ));
                    return new ResponseEntity<>(_adm, HttpStatus.CREATED);
                } catch (Exception e) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOAdmin> updateAdmin(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id, @RequestBody DTOAdmin adm) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ACTUALIZAR_ADMIN"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOAdmin> empData = aRepository.findById(id);
            if (empData.isPresent()) {
                Date fecha = Date.from(Instant.now());
                DTOAdmin _adm = empData.get();
                _adm.setModifiedby(idAccess);
                _adm.setPhone(adm.getPhone());
                _adm.setActive(adm.getActive());
                _adm.setListPermissions(adm.getListPermissions());
                _adm.setLastmodification(new java.sql.Date(fecha.getTime()));
                return new ResponseEntity<>(aRepository.save(_adm), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{idAccess}/{id}")
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        if (idAccess.equals(id)) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ELIMINAR_ADMIN"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                aRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/root")
    public ResponseEntity<DTOAdmin> createROOT(@RequestBody DTOAdmin adm) {
        if (true == checkIdsExist(adm.getListPermissions())) {
            try {
                DTOAdmin _adm = aRepository.save(new DTOAdmin(
                        adm.getName(),
                        adm.getLastname(),
                        adm.getPhone(),
                        adm.getActive(),
                        adm.getModifiedby(),
                        adm.getListPermissions()
                ));
                return new ResponseEntity<>(_adm, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
    }
}
