package com.tfmapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;
import com.tfmapp.model.repository.impl.ConfigureLdapConnectionRepositoryImpl;

@Service
public class ServiceAuthLdap {

    @Autowired
    private ConfigureLdapConnectionRepositoryImpl configureLdapConnectionRepositoryImpl;

    public void checkUserCredentials() {

        LdapContextSource sourceLdapCtx = this.configureLdapConnectionRepositoryImpl.configureLdapConnection();

        if (this.configureLdapConnectionRepositoryImpl.checkConnection(sourceLdapCtx)) {

        }
    }
}
