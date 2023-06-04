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
 */
@Data
@NoArgsConstructor()
@Entity
@Table(name = "admins")
public class DTOAdmin extends Persons {
    @ElementCollection
    @CollectionTable(name = "admins_permissions", joinColumns = @JoinColumn(name = "admin_id"))
    @Column(name = "permission")
    private List<String> listPermissions;

    public DTOAdmin(String name, String lastname, String phone, String active, String modifiedby, List<String> listPermissions) {
        super(name, lastname, phone, active, modifiedby);
        generateAdminCodes();
        this.listPermissions = listPermissions;
    }

    @PrePersist
    public void generateAdminCodes() {
        String firstNameInitials = getName().substring(0, Math.min(getName().length(), 2)).toUpperCase();
        String lastNameInitials = getLastname().substring(0, Math.min(getLastname().length(), 2)).toUpperCase();
        String randomNumbers = String.format("%02d", new Random().nextInt(100));
        String adminCode = "A" + firstNameInitials + lastNameInitials + randomNumbers;
        this.setId(adminCode);
        String[] nameParts = getName().split(" ");
        String firstName = nameParts[0].toUpperCase();
        String adminId = getId().toUpperCase();
        String usernameP  = firstName + "@" + adminId;
        this.setUsername(usernameP);
        String nameInvertido = new StringBuilder(firstName).reverse().toString();
        String passwordP  = "P" + nameInvertido + "@" +randomNumbers;
        this.setPassword(passwordP);
    }

    
}
