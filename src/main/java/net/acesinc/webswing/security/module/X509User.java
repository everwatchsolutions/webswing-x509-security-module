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
    private List<String> roles;
    private Map<String, Serializable> userAttributes;
    
    public X509User(String userId) {
        super();
        this.userId = userId;
        roles = new ArrayList<>();
        userAttributes = new HashMap<>();
    }
    
    public void addUserAttribute(String key, Serializable value) {
        userAttributes.put(key, value);
    }
    
    public Serializable getUserAttribute(String key) {
        return userAttributes.get(key);
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
    
}
