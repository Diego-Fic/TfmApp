package com.tfmapp.model.service.authentication;

import com.tfmapp.model.repository.data.ldap.UserLdap;

public interface ServiceAuthLdap {

    Boolean checkUserCredentials(UserLdap userLdap);

}
