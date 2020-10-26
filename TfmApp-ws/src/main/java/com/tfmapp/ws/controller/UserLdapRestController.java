package com.tfmapp.ws.controller;

import com.tfmapp.model.repository.data.ldap.UserLdap;

public interface UserLdapRestController {
    public Boolean checkUserLdapAuth(UserLdap userLdap);

}
