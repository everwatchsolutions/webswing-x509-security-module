/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.webswing.security.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  andrewserff
 */
public class ReflectionHelper {
    
    private static final Logger log = LoggerFactory.getLogger(ReflectionHelper.class);
    
    /**
     * Get class name without package prefix
     * @param application   An application object to get name for.
     * @return Class name without package prefix.
     */
    public static String getShortClassName(Object application) {
        String fullName = application.getClass().getName();
        return ReflectionHelper.getShortClassName(fullName);
    }
    
    /**
     * Get class name without package prefix
     * @param className The full class name you want the short version of.
     * @return Class name without package prefix.
     */
    public static String getShortClassName(String className) {
        int lastDot = className.lastIndexOf(".");
        return className.substring(lastDot+1, className.length());
    }
    /**
     * This method uses reflection to find the passed in classname.
     * The passed in classname MUST BE the full class name (including the 
     * packagename).  This method will create an object name by classname using
     * the default constructor.
     * @param classname the class you want to find
     * @return an instance of the object you passed in if found, null otherwise
     */
    public static Object createObject(String classname) {
        return createObject(classname, null, null);
    }
    
    /**
     * This method uses reflection to find the passed in classname.
     * The passed in classname MUST BE the full class name (including the 
     * packagename).  If you pass in null for consParams and consArgs, you will
     * invoke the default constructor of the object named by classname.  
     * If we do not find the class with the matching name and constructor, null
     * will be returned. 
     * @param classname the full packagename of the class you want to find
     * @param consParams the parameters of the constructor you want to invoke, can be null
     * @param consArgs the arguments you want to pass into the constructor, can be null
     * @return an instance of the object you passed in if found, null otherwise.
     */
    public static Object createObject(String classname, Class[] consParams, Object[] consArgs) {
        try {
            Class theClass = Class.forName(classname);
            Class[] params = {};
            if (consParams != null) {
                params = consParams;
            }
            
            Constructor con = theClass.getConstructor(params);
            
            Object[] args = {};
            if (consArgs != null) {
                args = consArgs;
            }
            
            Object obj = con.newInstance(args);
            return obj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Error instantiating class [ " + classname + " ]", e);
        }
        return null;
    }
}
