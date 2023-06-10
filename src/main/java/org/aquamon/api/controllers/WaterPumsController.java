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
import org.aquamon.api.dto.DTOWaterPump;
import org.aquamon.api.repositorys.AdminsRepository;
import org.aquamon.api.repositorys.EmployeesRepository;
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
 */
@RestController
@RequestMapping("/aquamon/pumps")
@CrossOrigin(origins = "*")
public class WaterPumsController {

    @Autowired
    AdminsRepository aRepository;
    @Autowired
    EmployeesRepository eRepository;
    @Autowired
    WaterPumpsRepository wRepository;

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
    public ResponseEntity<List<DTOWaterPump>> getAllWaterPumps(@PathVariable("idAccess") String idAccess) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_BOMBAS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOWaterPump> ltsWaP = new ArrayList<>();
                wRepository.findAll().forEach(ltsWaP::add);
                return new ResponseEntity<>(ltsWaP, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOWaterPump> getWaterPumpById(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_BOMBAS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOWaterPump> perData = wRepository.findById(id);
            if (perData.isPresent()) {
                return new ResponseEntity<>(perData.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/me/{idAccess}")
    public ResponseEntity<List<DTOWaterPump>> buscarPorAsignacion(@PathVariable("idAccess") String idAccess) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_BOMBAS_ASIGNADAS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOWaterPump> ltsWaP = wRepository.findByAssignment(idAccess);
                return new ResponseEntity<>(ltsWaP, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/assignments/{idAccess}/{asignacion}") 
    public ResponseEntity<List<DTOWaterPump>> buscarPorAsignacion(@PathVariable("idAccess") String idAccess, @PathVariable("asignacion") String asignacion) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_VER_BOMBAS"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                List<DTOWaterPump> ltsWaP = wRepository.findByAssignment(asignacion);
                return new ResponseEntity<>(ltsWaP, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/{idAccess}/")
    public ResponseEntity<DTOWaterPump> createWaterPump(@PathVariable("idAccess") String idAccess, @RequestBody DTOWaterPump waterPump) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_REGISTRAR_BOMBA"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                waterPump.setModifiedby(idAccess);
                DTOWaterPump _adm = wRepository.save(new DTOWaterPump(
                        waterPump.getName(),
                        waterPump.getLocation(),
                        waterPump.getEnabled(),
                        waterPump.getAssignment(),
                        waterPump.getModifiedby()
                ));
                return new ResponseEntity<>(_adm, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/{idAccess}/{id}")
    public ResponseEntity<DTOWaterPump> updateWaterPump(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id, @RequestBody DTOWaterPump waterPump) {
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ACTUALIZAR_BOMBA"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            Optional<DTOWaterPump> watData = wRepository.findById(id);
            if (watData.isPresent()) {
                Date fecha = Date.from(Instant.now());
                DTOWaterPump _waterPump = watData.get();
                _waterPump.setModifiedby(idAccess);

                _waterPump.setLocation(waterPump.getLocation());
                _waterPump.setEnabled(waterPump.getEnabled());
                _waterPump.setAssignment(waterPump.getAssignment());
                _waterPump.setLastmodification(new java.sql.Date(fecha.getTime()));

                return new ResponseEntity<>(wRepository.save(_waterPump), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{idAccess}/{id}")
    public ResponseEntity<HttpStatus> deleteWaterPump(@PathVariable("idAccess") String idAccess, @PathVariable("id") String id) {
        if (idAccess.equals(id)) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        AccessResult accessResult = getEntityByIdForAccess(idAccess, AccessCodes.getCode("CODE_ELIMINAR_BOMBA"));
        if (accessResult != null && accessResult.getAccessChecker().checkAccess(accessResult.getPermissions(), accessResult.getAccessCode())) {
            try {
                wRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

}
