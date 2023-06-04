/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rules;

import java.util.List;
import rules.interfaces.AccessChecker;

/**
 *
 * @author zS20006736
 */
public class AccessResult {

    private Object data;
    private AccessChecker accessChecker;
    private String accessCode;

    public AccessResult(Object data, AccessChecker accessChecker, String accessCode) {
        this.data = data;
        this.accessChecker = accessChecker;
        this.accessCode = accessCode;
    }

    public Object getData() {
        return data;
    }

    public AccessChecker getAccessChecker() {
        return accessChecker;
    }

    public String getAccessCode() {
        return accessCode;
    }
    
    public List<String> getPermissions() {
        return accessChecker.getPermissions(data);
    }
}
