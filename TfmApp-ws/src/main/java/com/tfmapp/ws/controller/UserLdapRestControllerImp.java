package com.tfmapp.ws.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tfmapp.model.repository.data.files_directory.Directory;
import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.authentication.ServiceAuthLdap;
import com.tfmapp.model.service.filesFinder.ServiceFilesFinder;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5050")
public class UserLdapRestControllerImp implements UserLdapRestController {

    @Autowired
    private ServiceAuthLdap serviceAuthLdap;

    @Autowired
    private ServiceFilesFinder serviceFilesFinder;

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkUserLdapAuth(UserLdap userLdap) {
        return this.serviceAuthLdap.checkUserCredentials(userLdap);
    }

    @RequestMapping(value = "/findDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDocuments() throws JsonProcessingException {
        ArrayList<Directory> lista_directorios = this.serviceFilesFinder.searchFiles();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(lista_directorios);
        return json;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDownloadFiles(@RequestBody String o) throws JsonProcessingException {

        JSONArray jsonArray = new JSONArray(o); 
        ArrayList<String> localizacion_directorios = this.serviceFilesFinder.getDownloadDocuments(jsonArray);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(localizacion_directorios);
        return json;

    }

}