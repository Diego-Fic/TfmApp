package com.tfmapp.model.repository.impl;

import java.util.logging.Logger;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Repository;


@Repository
public class ConfigureLdapConnectionRepositoryImpl {

    private final static Logger LOGGER = Logger.getLogger(ConfigureLdapConnectionRepositoryImpl.class.getName());

    public LdapContextSource configureLdapConnection() {

        LdapContextSource sourceLdapCtx = new LdapContextSource();
        sourceLdapCtx.setUrl("ldap://192.168.1.200");
        sourceLdapCtx.setUserDn("admin");
        sourceLdapCtx.setPassword("1234");
        sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
        sourceLdapCtx.afterPropertiesSet();

        return sourceLdapCtx;

    }

    public Boolean checkConnection(LdapContextSource sourceLdapCtx) {
        try {
            LOGGER.info("Connecting to LDAP: " + sourceLdapCtx.getUrls());
            LdapTemplate sourceLdapTemplate = new LdapTemplate(sourceLdapCtx);
            // Authenticate:
            sourceLdapTemplate.getContextSource().getContext(sourceLdapCtx.getUserDn(), sourceLdapCtx.getPassword());
        } catch (Exception e) {
            LOGGER.info("Failed to connect to LDAP - " + sourceLdapCtx.getUrls());
            return false;
        }
        return true;
    }
}
