package com.tfmapp.model.repository.data.ldap;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Repository
public class UserLdap implements Serializable {

    private static final long serialVersionUID = -3997128340382369006L;

    private String username;
    private String password;
}
