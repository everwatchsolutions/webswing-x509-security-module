## WebSwing x509 Security Module

This project provides a x509 (PKI/Client Certificate) authentication module for the [WebSwing](https://webswing.org) platform.  This allows you to authenticate users using credentials provided by a client certificate.  This module also provides a framework for providing a Authentication Provider using the provided certificate. It does nothing out of the box, so you must provide your own Authentication Provider that will populate user credentials so that WebSwing can restrict access to apps based on your custom roles/authorities. See below for more info on how to provide your own provider. 

### Project Status 

[![Build Status](https://travis-ci.org/acesinc/webswing-x509-security-module.svg?branch=master)](https://travis-ci.org/acesinc/webswing-x509-security-module)

### License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

### Usage

#### Prerequisites 

Please note that in order for this security module to work, you container must be configured to use SSL and require client authentication. WebSwing 2.5 using the embedded jetty container does not support this.  We submitted a [Pull Request](https://bitbucket.org/meszarv/webswing/pull-requests/23/add-configuration-option-to-enable/diff) that adds client authentication support to WebSwing. If they haven't merged this into WebSwing yet and you want to use this security module, you can clone the PR and build it locally. In order to use this with the built in Jetty, your `jetty.properties` should look something like:

```
org.webswing.server.host=localhost

org.webswing.server.http=false
org.webswing.server.http.port=8080

org.webswing.server.https=true
org.webswing.server.https.port=8443
oorg.webswing.server.https.truststore=ssl/truststore.jks
org.webswing.server.https.truststore.password=123123
org.webswing.server.https.keystore=ssl/keystore.jks
org.webswing.server.https.keystore.password=123123
org.webswing.server.https.clientAuthEnabled=true
```

The `org.webswing.server.https.clientAuthEnabled=true` being the important change. 

After updating these properties, make sure you restart Jetty/WebSwing.

#### Installiation of Security Module

To include this security module into your WebSwing installiation:

First, download the latest release or clone the repo and build it.  Next you need to copy the jar file to your WebSwing installiation.  You don't have to put the jar in the locations we choose below, but this how we set it up.  

Create a directory for the module:

```
mkdir -p ${WEBSWING_HOME}/security/lib
cp webswing-x509-security-module-1.0.0.jar ${WEBSWING_HOME}/security/lib
```

Once you have copied the security module into the WebSwing installiation, you need to configure an application to use it. Open your WebSwing console and open an applications configuration options.  Under the Security, set the following options:

* Security Module Name: `net.acesinc.webswing.security.module.X509SecurityModule`
* Security Module Class Path: `${webswing.rootDir}/security/lib/webswing-x509-security-module-1.0.0-SNAPSHOT.jar`

Later you will need to add your own custom jar to the classpath here.  

Under `Security Module Config - General` you will see two options: 

* User Authentication Service Provider
* Authentication Service Provider Options 

You will provide a Authentication Service Provider later, but if you want to test it out with one that will just print out some info, you can use `net.acesinc.webswing.security.module.NoOpUserAuthService` as the `User Authentication Service Provider`.  

The `Authentication Service Provider Options` allow you to pass a Map of key/value pairs to your own custom Authentication Provider. These are optional and are up to your own plugin needs.  

Once you have configured the security module, make sure you apply the changes. You may need to restart WebSwing to get the changes to take affect. 


### Building your custom Authentication Provider

As mentioned, this modules provides a framework for providing your own custom Authentication Provider that will help you populate the User object with all the roles/authorities they need.  Therefore, you need to provide an implementation of a `X509UserAuthorizationService`.  This `abstract class` defines two things: 

* A way to pass Configuration Options into the Provider
* A single method, `X509User populateUserAuthorizations(X509User user)`, that is called by the `X509SecurityModule` to populate the User object with their roles/authorities.  

The `X509UserAuthorizationService` is provided by the following dependency:

```
<dependency>
  <groupId>net.acesinc.webswing</groupId>
  <artifactId>webswing-x509-security-module</artifactId>
  <version>1.0.0</version>
</dependency>
```
Below is an example of a Provider:

```
public class NoOpUserAuthService extends X509UserAuthorizationService {
    
    private static final Logger log = LoggerFactory.getLogger(NoOpUserAuthService.class);

    @Override
    public X509User populateUserAuthorizations(X509User user) {
        log.warn("NoOpUserAuthService doing nothing to populate user authorizations for user [ " + user.getUserId() + " ]");
        log.info("The following options are set: " + this.getOptions());
        return user;
    }
    
}
```

Note that the X509User provides access to a userId and also the full CN though the `getFullCn()` method. 

Assuming that you create your own jar file with your Authentication Provider in it, you need to add this jar to the Security Module classpath as we mentioned above.  Once you have added it, ensure you update the `User Authentication Service Provider` to the fully qualified class name of your provider. Also add any options you might need to the config as well and apply your changes.  

Once you have done all this, when a user trys to launch you application, the users Certificate will be extracted and the CN is passed to your Authentication Provider.  Your Provider should look up the user, populate the user object by using the `addRole(role)` or `addUserAttribute(key,value)`. If the user is invalid, return `null` from the `populateUserAuthorities` method.

