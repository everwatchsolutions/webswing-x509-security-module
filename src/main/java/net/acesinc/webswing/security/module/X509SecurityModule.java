/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.services.security.api.AbstractWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.modules.AbstractExtendableSecurityModule;

/**
 *
 * @author andrewserff
 */
public class X509SecurityModule extends AbstractExtendableSecurityModule<X509SecurityModuleConfig> {

    private static final Logger log = LoggerFactory.getLogger(X509SecurityModule.class);

    private X509UserAuthorizationService userAuthService;

    public X509SecurityModule(X509SecurityModuleConfig config) {
        super(config);

        this.userAuthService = locateUserAuthService();
    }

    @Override
    protected AbstractWebswingUser authenticate(HttpServletRequest hsr) throws WebswingAuthenticationException {
        log.info("authenticate was called");
        Principal p = getPrincipal(hsr);
        X509User user = null;
        if (p != null) {
            String userId = null;
            String commonName = parseCommonNameFromDn(p.getName());
            String[] strings = commonName.split(" ");

            // UserID
            if (strings.length > 0) {
                userId = strings[strings.length - 1];
            }

            log.info("Authenticating user [ " + userId + " ]");
            user = new X509User(userId);
            log.debug("Created User object for user [ " + user.getUserId() + " ]");

            if (userAuthService != null) {
                user = userAuthService.populateUserAuthorizations(user);
            }
        } else {
            log.warn("No user info was found in request to authenticate...");
            log.debug("oh well -- debug");
        }
        return user;
    }

    @Override
    protected void serveLoginPartial(HttpServletRequest hsr, HttpServletResponse hsr1, WebswingAuthenticationException wae) throws IOException {
        //we don't need a login page...
    }

    /**
     * Extract the Principal from the request
     *
     * @param req the HttpServletRequest to extract the principal from
     * @return the Principal if one exists, null otherwise
     */
    protected Principal getPrincipal(HttpServletRequest req) {
        X509Certificate principalCert = extractCertificate(req);
        Principal principal = null;
        if (principalCert != null) {
            principal = principalCert.getSubjectDN();
        }
        return principal;

    }

    /**
     * Extracts the client certificate from the
     * javax.servlet.request.X509Certificate request attribute
     *
     * @param req The HttpServletRequest to extract the certificate from
     * @return The client certificate if it exists, null otherwise.
     */
    protected X509Certificate extractCertificate(HttpServletRequest req) {
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
        if (null != certs && certs.length > 0) {
            return certs[0];
        } else {
            return null; //no cert found in req
        }

    }

    public static String parseCommonNameFromDn(String dnOrCn) {
        if (dnOrCn == null) {
            log.error("Unable to parse commonName from null DN value.");
            return null;
        } else if (!dnOrCn.toUpperCase().contains("CN=")) {
            return dnOrCn;
        }

        String commonName = null;
        try {
            LdapName ldapName = new LdapName(dnOrCn);
            for (Rdn rdn : ldapName.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    commonName = (String) rdn.getValue();
                    break;
                }
            }
        } catch (InvalidNameException e) {
            log.error("Unable to parse commonName from DN [" + dnOrCn + "]", e);
        }

        return commonName;
    }

    private X509UserAuthorizationService locateUserAuthService() {
//        ClassFinder finder = new ClassFinder();
//        finder.addClassPath();
//
//        ClassFilter filter = new SubclassClassFilter(X509UserAuthorizationService.class);
//
//        Collection<ClassInfo> foundClasses = new ArrayList<ClassInfo>();
//        finder.findClasses(foundClasses, filter);
//
//        log.debug("Found [ " + foundClasses.size() + " ] UserAuthenticationService Providers");
//        for (ClassInfo classInfo : foundClasses) {
//            String className = classInfo.getClassName();
        String className = this.getConfig().getUserAuthClassName();
        X509UserAuthorizationService authService = null;

        if (className != null && !className.isEmpty()) {
            log.info("Creating instance of [ " + className + " ]");
            authService = (X509UserAuthorizationService) ReflectionHelper.createObject(className);
            if (authService == null) {
                log.warn("No User Authentication Service Providers were found in ClassPath");
            }
        }
        return authService;
//        }
    }

}
