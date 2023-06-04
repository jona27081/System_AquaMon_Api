/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.controllers;

import java.time.Instant;
import rules.AccessCodes;
import java.util.ArrayList;
import java.util.Date;
import org.aquamon.api.repositorys.PermissionsRepository;
import org.aquamon.api.repositorys.EmployeesRepository;
import java.util.List;
import java.util.Optional;
import org.aquamon.api.dto.DTOAdmin;
import org.aquamon.api.dto.DTOEmployee;
import org.aquamon.api.dto.DTOPermission;
import org.aquamon.api.repositorys.AdminsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rules.AccessResult;
import rules.DTOAdminAccessChecker;
import rules.DTOEmployeeAccessChecker;

/**
 *
 * @author zS20006736
 En versiones posteriores implementar un metodo que asigne automaticamente las bombas que el empleado tiene a su asignacion,
 * Tendremos un metodo que recorra la lista de bombas buscando en el campo assignment el id del empleado, las que encuentre las guardara en una lista
 * y por utimo la asignara la lista se pasara como parametro al metodo setUnityList
 De momento el campo en el DTO de empleados se encuentra comentado para omiir su implementacion
 * Nota en ApiAquaMon version 0.0.1
 */
@RestController
@RequestMapping("/aquamon/employees")
public class EmployeesController {

    @Autowired
    EmployeesRepository eRepository;
    @Autowired
    PermissionsRepository pRepository;
    @Autowired
    AdminsRepository aRepository;

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
    public ResponseEntity<List<DTOEmployee>> getAllEmployees(@PathVariable("idAccess") String idAccess) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_EMPLEADOS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOEmployee> ltsEmps = new ArrayList<>();
                eRepository.findAll().forEach(ltsEmps::add);
                return new ResponseEntity<>(ltsEmps, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOEmployee> getEmployeeById(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_EMPLEADOS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOEmployee> perData = eRepository.findById(id);
            if (perData.isPresent()) {
                return new ResponseEntity<>(perData.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/{idAccess}/")
    public ResponseEntity<DTOEmployee> createEmployee(@PathVariable("idAccess") String idAccess, @RequestBody DTOEmployee emp) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_REGIS_EMPLE"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            if (true == checkIdsExist(emp.getListPermissions())) {
                try {
                    emp.setModifiedby(idAccess);
                    DTOEmployee _emp = eRepository.save(new DTOEmployee(
                            emp.getName(),
                            emp.getLastname(),
                            emp.getPhone(),
                            emp.getActive(),
                            emp.getModifiedby(),
                            emp.getListPermissions()                            
                    ));
                    return new ResponseEntity<>(_emp, HttpStatus.CREATED);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOEmployee> updateEmployee(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id, @RequestBody DTOEmployee emp) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ACTUALIZAR_EMPLEADO"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOEmployee> empData = eRepository.findById(id);
            if (empData.isPresent()) {
                Date fecha = Date.from(Instant.now());
                DTOEmployee _emp = empData.get();
                _emp.setModifiedby(idAccess);
                _emp.setPhone(emp.getPhone());                
                _emp.setActive(emp.getActive());
                _emp.setListPermissions(emp.getListPermissions());
                _emp.setLastmodification(new java.sql.Date(fecha.getTime()));                
                return new ResponseEntity<>(eRepository.save(_emp), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{idAccess}/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        if (idAccess.equals(id)) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ELIMINAR_EMPLEADO"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                eRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

}
