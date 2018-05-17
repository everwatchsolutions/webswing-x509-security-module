/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.webswing.server.services.security.api.AbstractWebswingUser;

/**
 *
 * @author andrewserff
 */
public class X509User extends AbstractWebswingUser {

    private String userId;
    private String fullCn;
    private List<String> roles;
    private Map<String, Serializable> userAttributes;
    
    public X509User(String userId, String fullCn) {
        super();
        this.userId = userId;
        this.fullCn = fullCn;
        roles = new ArrayList<>();
        userAttributes = new HashMap<>();
    }
    
    public void addUserAttribute(String key, Serializable value) {
        userAttributes.put(key, value);
    }
    
    public Serializable getUserAttribute(String key) {
        return userAttributes.get(key);
    }
    
    public void addRole(String role) {
        roles.add(role);
    }
    
    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public Map<String, Serializable> getUserAttributes() {
        return userAttributes;
    }

    @Override
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
    
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @return the fullCn
     */
    public String getFullCn() {
        return fullCn;
    }

    /**
     * @param fullCn the fullCn to set
     */
    public void setFullCn(String fullCn) {
        this.fullCn = fullCn;
    }
    
}
