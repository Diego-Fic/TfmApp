package com.tfmapp.model.service.usersLdap;

import java.util.ArrayList;

import javax.naming.NamingException;

import org.json.JSONArray;

public interface UpdateDBWithLdapData {

    public Boolean newConnection();
    
    public JSONArray getAllAlumnos() throws NamingException;
}
