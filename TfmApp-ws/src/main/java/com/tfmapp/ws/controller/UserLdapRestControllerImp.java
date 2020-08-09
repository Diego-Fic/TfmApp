package com.tfmapp.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.authentication.ServiceAuthLdap;



@RestController
@CrossOrigin(origins = "http://localhost:5050")
public class UserLdapRestControllerImp {

    @Autowired
    private ServiceAuthLdap serviceAuthLdap;

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkUserLdapAuth(UserLdap userLdap) {
        return this.serviceAuthLdap.checkUserCredentials(userLdap);
    }
    
}