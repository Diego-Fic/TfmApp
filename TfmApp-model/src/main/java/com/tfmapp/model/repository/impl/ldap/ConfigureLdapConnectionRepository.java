package com.tfmapp.model.repository.impl.ldap;

import org.springframework.ldap.core.support.LdapContextSource;

import com.tfmapp.model.repository.data.ldap.UserLdap;

public interface ConfigureLdapConnectionRepository {

    LdapContextSource configureLdapConnection();

    Boolean checkConnection(LdapContextSource sourceLdapCtx);

    Boolean checkLoginUserCredentials(LdapContextSource sourceLdapCtx, UserLdap userLdap);
}
