package com.tfmapp.ws.controller.ldap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.xml.ws.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tfmapp.model.repository.data.files_directory.Directory;
import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.authentication.ServiceAuthLdap;
import com.tfmapp.model.service.filesFinder.ServiceFilesFinder;

@RestController
@CrossOrigin(origins = "http://localhost:5050")
public class UserLdapRestControllerImp implements UserLdapRestController {

    @Autowired
    private ServiceAuthLdap serviceAuthLdap;

    @Autowired
    private ServiceFilesFinder serviceFilesFinder;

    /*
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkUserLdapAuth(UserLdap userLdap) {
        return this.serviceAuthLdap.checkUserCredentials(userLdap);
    }
     */
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkUserLdapAuth(@RequestBody String string) throws JSONException, NamingException {
        JSONObject jsonObject = new JSONObject(string);
        UserLdap userLdap = new UserLdap();
        userLdap.setUsername(jsonObject.getString("username"));
        userLdap.setPassword(jsonObject.getString("password"));
        return this.serviceAuthLdap.checkUserCredentials(userLdap).toString();
    }

    @RequestMapping(value = "/findDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDocuments() throws JsonProcessingException {
        ArrayList<Directory> lista_directorios = this.serviceFilesFinder.searchFiles();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(lista_directorios);
        return json;
    }


    /*
    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDownloadFiles(@RequestBody String o) throws JsonProcessingException {

        JSONArray jsonArray = new JSONArray(o); 
        ArrayList<String> localizacion_directorios = this.serviceFilesFinder.getDownloadDocuments(jsonArray);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(localizacion_directorios);
        return json;
    */

        @RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        public ResponseEntity<byte[]> getFile(@RequestParam String json) throws IOException {
            
            JSONObject jsonObject = new JSONObject(json);
            
            String path = System.getProperty("user.dir");
            String[] splitPath = path.split("\\\\");
            List<String> result = new ArrayList<>();
            String dirFilesPath = "FrontEnd/cliente-web/src/assets/db/" + jsonObject.getString("temario") + "/" + jsonObject.getString("nombre");

            for (int i = 0; i < splitPath.length - 1; i++) {
                result.add(splitPath[i]);
            }
            result.add(dirFilesPath);
            String route = result.stream().map(Object::toString).collect(Collectors.joining("/"));

            File f = new File(route);
            byte[] file = Files.readAllBytes(f.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment; filename=" + f.getName());
            return new ResponseEntity(file, headers, HttpStatus.OK);
        }
    
    }
