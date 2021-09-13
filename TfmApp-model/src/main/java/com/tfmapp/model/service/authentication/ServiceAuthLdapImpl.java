package com.tfmapp.model.service.authentication;

import java.io.IOException;

import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;

import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.usersLdap.UpdateDBWithLdapDataImpl;

@Service
public class ServiceAuthLdapImpl implements ServiceAuthLdap {

    @Autowired
    private UpdateDBWithLdapDataImpl updateDBWithLdapDataImpl;
    
    public JSONObject checkUserCredentials(UserLdap userLdap) throws JSONException, NamingException {
        JSONObject json = new JSONObject();
        json.put("username", userLdap.getUsername());
        json.put("rol", " ");
        json.put("error", "false");
        json.put("error_msg", " ");
        
        if (updateDBWithLdapDataImpl.newConnection()) {
           if (checkUserCredentialsByFreeradius(userLdap)) {
               if (this.updateDBWithLdapDataImpl.searchAlumno(userLdap.getUsername())) {
                   json.put("rol", "Alumno");
               } else if (this.updateDBWithLdapDataImpl.searchProfesor(userLdap.getUsername())) {
                   json.put("rol", "Profesor");
               }
           } else {
                    json.put("error", "true");
                    json.put("error_msg", "Credenciales incorrectas");
           }
       } else {
            json.put("error", "true");
            json.put("error_msg", "Conexión con el servidor interrumpida");
        }
       
        return json;
    }
    
    private Boolean checkUserCredentialsByFreeradius(UserLdap userLdap) {
        RadiusClient radiusClient = new RadiusClient("192.168.1.200", "testing123");
        AccessRequest accessRequest = new AccessRequest(userLdap.getUsername(), userLdap.getPassword());
        try{
            RadiusPacket response = radiusClient.authenticate(accessRequest);
            //System.out.println(response.getPacketType());
            if(response.getPacketType() == RadiusPacket.ACCESS_ACCEPT){
                System.out.println("Authenticated");
                return true;
            }
            else if(response.getPacketType() == RadiusPacket.ACCESS_REJECT){
                System.out.println("No Authenticated");
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(RadiusException e){
            e.printStackTrace();
        }
        return false;        
        
    }

}
