package com.tfmapp.model.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.authentication.ServiceAuthLdap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ServiceAuthConfigTest.class})
public class ServiceAuthLdapTest {
    
    @Autowired
    private ServiceAuthLdap serviceAuthLdap;

    @Test
    public void checkConnection() {
        String userName = "Pepe";
        String password = "1234";
        
        UserLdap userLdap = new UserLdap();
        userLdap.setUsername(userName);
        userLdap.setPassword(password);
        
        Boolean verified_user = this.serviceAuthLdap.checkUserCredentials(userLdap);
        assertTrue(verified_user);
    }
}
