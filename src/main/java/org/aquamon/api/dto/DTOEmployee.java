/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.aquamon.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.*;
import lombok.*;
import java.lang.StringBuilder;

/**
 *
 * @author zS20006736
    
    @ElementCollection
    @CollectionTable(name = "employees_unity_list", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "unity")
    private List de string unityList;
 
 */
@Data
@NoArgsConstructor()
@Entity
@Table(name = "employees")
public class DTOEmployee extends Persons {
    @ElementCollection
    @CollectionTable(name = "employees_permissions", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "permission")
    private List<String> listPermissions;

    public DTOEmployee(String name, String lastname, String phone, String active, String modifiedby, List<String> listPermissions) {
        super(name, lastname, phone, active, modifiedby);
        generateEmployeeCodes();
        this.listPermissions = listPermissions;
    }

    @PrePersist
    public void generateEmployeeCodes() {
        String firstNameInitials = getName().substring(0, Math.min(getName().length(), 2)).toUpperCase();
        String lastNameInitials = getLastname().substring(0, Math.min(getLastname().length(), 2)).toUpperCase();
        String randomNumbers = String.format("%02d", new Random().nextInt(100));
        String employeeCode = "E" + firstNameInitials + lastNameInitials + randomNumbers;
        this.setId(employeeCode);
        String[] nameParts = getName().split(" ");
        String firstName = nameParts[0].toUpperCase();
        String employeeId = getId().toUpperCase();
        String usernameP  = firstName + "@" + employeeId;
        this.setUsername(usernameP);
        String nameInvertido = new StringBuilder(firstName).reverse().toString();
        String passwordP  = "P" + nameInvertido + "@" +randomNumbers;
        this.setPassword(passwordP);
    }

}
