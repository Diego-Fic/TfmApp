package com.tfmapp.model.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.repository.impl.ldap.ConfigureLdapConnectionRepository;
import com.tfmapp.model.repository.impl.ldap.ConfigureLdapConnectionRepositoryImpl;

@Service
public class ServiceAuthLdapImpl implements ServiceAuthLdap {

    @Autowired
    private ConfigureLdapConnectionRepository configureLdapConnectionRepository;

    public Boolean checkUserCredentials(UserLdap userLdap) {

        LdapContextSource sourceLdapCtx = this.configureLdapConnectionRepository.configureLdapConnection();

        if (this.configureLdapConnectionRepository.checkConnection(sourceLdapCtx)) {
            if (this.configureLdapConnectionRepository.checkLoginUserCredentials(sourceLdapCtx, userLdap)) {
                return true;
            } else
                return false;
        }
        return false;
    }
}
