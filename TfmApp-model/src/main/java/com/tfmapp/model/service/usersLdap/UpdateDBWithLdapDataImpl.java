package com.tfmapp.model.service.usersLdap;

import java.util.ArrayList;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;



@Service
public class UpdateDBWithLdapDataImpl implements UpdateDBWithLdapData{

    
    private DirContext connection;

    /* create connection during object creation */
    public Boolean newConnection() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://192.168.1.200:389");
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=ldap,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, "1234");
        try {
            connection = new InitialDirContext(env);
            System.out.println("Hello World!" + connection);
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
            return false;
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public JSONArray getAllAlumnos() throws NamingException {
        JSONArray returnValue = new JSONArray();
        String searchFilter = "(objectClass=inetOrgPerson)";
        String[] reqAtt = { "sn" };
        SearchResult result = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAtt);

        NamingEnumeration<SearchResult> users = connection.search("cn=Alumnos,ou=Universidad,dc=ldap,dc=com", searchFilter, controls);

        while (users.hasMore()) {
            JSONObject newObject = new JSONObject();
            result = (SearchResult) users.next();
            Attributes attr = result.getAttributes();
            newObject.put("nombre_alumno_ldap",attr.get("sn").get(0).toString());
            returnValue.put(newObject);
        }
        return returnValue;
    }
    
    public boolean searchAlumno(String username) throws NamingException {
        String searchFilter = "(sn= "+username+")";
        String[] reqAtt = { "sn" };
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAtt);

        NamingEnumeration<SearchResult> users = connection.search("cn=Alumnos,ou=Universidad,dc=ldap,dc=com", searchFilter, controls);

        SearchResult result = null;
        if (users.hasMore()) {
            while (users.hasMore()) {
                result = (SearchResult) users.next();
                Attributes attr = result.getAttributes();
                System.out.println(users);
            }
            return true;
        } else 
            return false;
    }
    
    public boolean searchProfesor(String username) throws NamingException {
        String searchFilter = "(sn= "+username+")";
        String[] reqAtt = { "sn" };
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAtt);

        NamingEnumeration<SearchResult> users = connection.search("cn=Profesores,ou=Universidad,dc=ldap,dc=com", searchFilter, controls);

        SearchResult result = null;
        if (users.hasMore()) {
            while (users.hasMore()) {
                result = (SearchResult) users.next();
                Attributes attr = result.getAttributes();
                System.out.println(users);
            }
            return true;
        } else 
            return false;
    }
}
