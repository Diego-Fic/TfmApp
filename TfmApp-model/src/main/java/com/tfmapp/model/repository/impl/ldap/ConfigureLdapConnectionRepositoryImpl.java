package com.tfmapp.model.repository.impl.ldap;

import java.util.List;
import java.util.logging.Logger;

import javax.naming.TimeLimitExceededException;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

import com.tfmapp.model.mapper.UserLdapMapper;
import com.tfmapp.model.repository.data.ldap.UserLdap;

@Repository
public class ConfigureLdapConnectionRepositoryImpl implements ConfigureLdapConnectionRepository{

    private final static Logger LOGGER = Logger.getLogger(ConfigureLdapConnectionRepositoryImpl.class.getName());

    public LdapContextSource configureLdapConnection() {

        // cn=admin,dc=ldap,dc=com
        // ou=Universidades,cn=Alumnos,cn=Pepe
        LdapContextSource sourceLdapCtx = new LdapContextSource();
        sourceLdapCtx.setUrl("ldap://192.168.1.200:389/");
        sourceLdapCtx.setUserDn("cn=admin,dc=ldap,dc=com");
        sourceLdapCtx.setPassword("1234");
        sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
        sourceLdapCtx.afterPropertiesSet();

        return sourceLdapCtx;

    }

    public Boolean checkConnection(LdapContextSource sourceLdapCtx) {
        try {
            LdapTemplate sourceLdapTemplate = new LdapTemplate(sourceLdapCtx);
            // Authenticate:
            sourceLdapTemplate.getContextSource().getContext(sourceLdapCtx.getUserDn(), sourceLdapCtx.getPassword());
            LOGGER.info("Connect to LDAP - " + sourceLdapCtx.getUrls());
        } catch (Exception e) {
            LOGGER.info("Failed to connect to LDAP - " + sourceLdapCtx.getUrls());
            return false;
        }
        return true;
    }

    public Boolean checkLoginUserCredentials(LdapContextSource sourceLdapCtx,UserLdap userLdap) {
            //base – specifies the root DN in the LDAP tree where the search should start.
            //searchScope – specifies how deep into the LDAP tree the search should traverse.
            //attributes – specifies the attributes to return from the search. Default is all.
            //countLimit – specifies the maximum number of entries to return from the search.
            //timeLimit – specifies the maximum time that the search may take.
            //Search Filter – the conditions that the entries we are looking for must meet.
        try {
            LdapTemplate sourceLdapTemplate = new LdapTemplate(sourceLdapCtx);
            LdapQuery queryUserLogin = LdapQueryBuilder.query().base("cn=Alumnos,ou=Universidad,dc=ldap,dc=com")
                    .where("objectClass").is("person")
                    .and("sn").like(userLdap.getUsername())
                    .and("userPassword").like(userLdap.getPassword());
            // Find User Login Credentials:
            List<UserLdap> listUserLogin =  sourceLdapTemplate.search(queryUserLogin, new UserLdapMapper());
            if (listUserLogin.isEmpty() || listUserLogin.size() > 1) {
                LOGGER.info("Failed to find user login credentials");
                return false;
            } else {
                UserLdap userLoginLdap = listUserLogin.get(0);
                LOGGER.info("Find user login credentials");
                return userLoginLdap.equals(userLdap);
            }
        } catch (Exception e) {
            LOGGER.info("Error trying to find user login credentials");
            return false;
        }
    }
}
