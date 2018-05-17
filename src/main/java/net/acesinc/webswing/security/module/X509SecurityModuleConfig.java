/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldOrder;
import org.webswing.server.common.model.meta.ConfigFieldVariables;
import org.webswing.server.common.model.meta.VariableSetName;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;

/**
 *
 * @author andrewserff
 */
@ConfigFieldOrder({"userAuthClassName"})
public interface X509SecurityModuleConfig extends WebswingExtendableSecurityModuleConfig {

    @ConfigField(label = "User Authentication Service Provider", description = "The fully qualified class name of the X509UserAuthenticationService you want to use to authorize users. Ensure the class is on your security module classpath")
    @ConfigFieldVariables(VariableSetName.Basic)
    String getUserAuthClassName();
}
