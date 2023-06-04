/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rules;

import java.util.List;
import org.aquamon.api.dto.DTOEmployee;
import rules.interfaces.AccessChecker;

/**
 *
 * @author zS20006736
 */
public class DTOEmployeeAccessChecker implements AccessChecker {
    @Override
    public boolean checkAccess(List<String> permissions, String accessCode) {
        // Lógica de verificación de acceso para DTOEmployee
        return permissions.contains(accessCode);
    }
    
    @Override
    public List<String> getPermissions(Object data) {
        DTOEmployee employee = (DTOEmployee) data;
        return employee.getListPermissions();
    }
}
