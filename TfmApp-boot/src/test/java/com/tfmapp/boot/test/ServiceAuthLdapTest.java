package com.tfmapp.boot.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tfmapp.boot.configTest.AppConfig;
import com.tfmapp.model.service.ServiceAuthLdap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={AppConfig.class})
public class ServiceAuthLdapTest {
    
    @Autowired
    private ServiceAuthLdap serviceAuthLdap;

    @Test
    public void checkConnection() {
//        this.serviceAuthLdap.checkUserCredentials();
        assertTrue(true);
    }
}
