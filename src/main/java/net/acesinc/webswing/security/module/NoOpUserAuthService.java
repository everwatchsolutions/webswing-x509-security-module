/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class NoOpUserAuthService extends X509UserAuthorizationService {
    
    private static final Logger log = LoggerFactory.getLogger(NoOpUserAuthService.class);

    @Override
    public X509User populateUserAuthorizations(X509User user) {
        log.warn("NoOpUserAuthService doing nothing to populate user authorizations for user [ " + user.getUserId() + " ]");
        log.info("The following options are set: " + this.getOptions());
        return user;
    }
    
}
