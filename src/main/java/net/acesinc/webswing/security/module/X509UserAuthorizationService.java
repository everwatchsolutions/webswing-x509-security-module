/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import java.util.Map;

/**
 *
 * @author andrewserff
 */
public abstract class X509UserAuthorizationService {
    private Map<String, String> options;

    public X509UserAuthorizationService() {
    }

    public X509UserAuthorizationService(Map<String, String> options) {
        this.options = options;
    }
    
    public abstract X509User populateUserAuthorizations(X509User user);

    /**
     * @return the options
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
