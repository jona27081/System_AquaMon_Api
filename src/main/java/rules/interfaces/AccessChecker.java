/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rules.interfaces;

import java.util.List;

/**
 *
 * @author zS20006736
 */
public interface AccessChecker {
    boolean checkAccess(List<String> permissions, String accessCode);
    List<String> getPermissions(Object data);
}
