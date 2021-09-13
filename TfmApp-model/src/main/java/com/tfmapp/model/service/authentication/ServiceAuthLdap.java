package com.tfmapp.model.service.authentication;

import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tfmapp.model.repository.data.ldap.UserLdap;


public interface ServiceAuthLdap {

    JSONObject checkUserCredentials(UserLdap userLdap) throws JSONException, NamingException;


}
