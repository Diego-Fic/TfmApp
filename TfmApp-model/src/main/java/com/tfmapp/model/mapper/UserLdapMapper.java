package com.tfmapp.model.mapper;

import java.util.Base64;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.fasterxml.jackson.databind.Module.SetupContext;
import com.tfmapp.model.repository.data.ldap.UserLdap;

public class UserLdapMapper implements AttributesMapper<UserLdap> {

    @Override
    public UserLdap mapFromAttributes(Attributes attrs) throws NamingException {
        UserLdap userLdap = new UserLdap();
        userLdap.setUsername(((String) attrs.get("cn").get()).trim());
        userLdap.setPassword( bitArrayToString(attrs.get("userPassword").get()));
        return userLdap;
    }
    
    private String bitArrayToString(Object o) {
        byte[] bytes = (byte[]) o;
        String value = new String(bytes).trim();
        return value;
    }
}
