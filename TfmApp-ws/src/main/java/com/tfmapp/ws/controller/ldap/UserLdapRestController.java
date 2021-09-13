package com.tfmapp.ws.controller.ldap;

import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserLdapRestController {
    
    public String checkUserLdapAuth(@RequestBody String string) throws JSONException, NamingException;

}
