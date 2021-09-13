package com.tfmapp.ws.controller.db;

import java.io.IOException;
import java.io.InputStream;

import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tfmapp.model.repository.data.ldap.UserLdap;
import com.tfmapp.model.service.db.ServiceDB;
import com.tfmapp.model.service.usersLdap.UpdateDBWithLdapData;


@RestController
@CrossOrigin(origins = "http://localhost:5050")
public class DBRestControllerImpl implements DBRestController{
    
    @Autowired
    private ServiceDB serviceDB;
    
    @Autowired
    private UpdateDBWithLdapData updateDBWithLdapData;
    
    // SELECTS
    
    @Override
    @RequestMapping(value = "/getConexiones", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConexiones() throws NamingException {
        return this.serviceDB.getConexiones().toString();
    }
    
    @Override
    @RequestMapping(value = "/getConexionesByGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConexionesByGrupo(@RequestBody String string) throws NamingException {
        JSONObject jsonObject = new JSONObject(string);
        String grupo = jsonObject.getString("grupo");
        return this.serviceDB.getConexionesByGrupo(grupo).toString();
    }
    
    @Override
    @RequestMapping(value = "/getAlumnosLdap", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAlumnosLdap() throws NamingException {
        this.updateDBWithLdapData.newConnection();
        JSONArray alumnosLdap = this.updateDBWithLdapData.getAllAlumnos(); 
        this.serviceDB.insertAlumnos(alumnosLdap);
    }
    
    @Override
    @RequestMapping(value = "/getAlumnos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAlumnos() {
        return this.serviceDB.getAlumnos().toString();
    }
    
    @Override
    @RequestMapping(value = "/getGrupos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGrupos() {
        return this.serviceDB.getGrupos().toString();
    }
    
    @Override
    @RequestMapping(value = "/getPods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPods() {
        return this.serviceDB.getPods().toString();
    }
    
    @Override
    @RequestMapping(value = "/getDispositivos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDispositivos() {
        return this.serviceDB.getDispositivos().toString();
    }
    
    @Override
    @RequestMapping(value = "/getMediaLatencias", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMediaLatencias() {
        return this.serviceDB.getMediaLatencias().toString();
    }
    
    @Override
    @RequestMapping(value = "/getConexionesMes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConexionesPorMes() {
        return this.serviceDB.getConexionesMes().toString();
    }
    
    @Override
    @RequestMapping(value = "/getAsignacionAlumnoGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAsignacionAlumnoGrupo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_alumno = jsonObject.getString("nombre_alumno");
        return this.serviceDB.getAsignacionAlumnoGrupo(nombre_alumno).toString();
    }
    
    @Override
    @RequestMapping(value = "/getAsignacionGrupoPod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAsignacionGrupoPod(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        return this.serviceDB.getAsignacionGrupoPodByGrupo(nombre_grupo).toString();
    }
    
    @Override
    @RequestMapping(value = "/getReservasByGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getReservasByGrupo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        return this.serviceDB.getReservasByGrupo(nombre_grupo).toString();
    }
    // INSERTS

    @Override
    @RequestMapping(value = "/insertReserva", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertReserva(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        String fecha_ini = jsonObject.getString("fecha_ini");
        String fecha_fin = jsonObject.getString("fecha_fin");
        String nombre = jsonObject.getString("nombre");
        String color = jsonObject.getString("color");
        String pod = jsonObject.getString("pod");
        //JSONObject pod = this.serviceDB.getAsignacionGrupoPodByGrupo(nombre_grupo); .getString("pod")
        this.serviceDB.insertReserva(nombre_grupo,pod,fecha_ini,fecha_fin,nombre,color);
    }
    
    @Override
    @RequestMapping(value = "/insertGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertGrupos(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        this.serviceDB.insertGrupo(nombre_grupo);
    }

    @Override
    @RequestMapping(value = "/insertPods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertPods(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_pod = jsonObject.getString("nombre_pod");
        this.serviceDB.insertPods(nombre_pod);
    }

    @Override
    @RequestMapping(value = "/insertDispositivo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertDispositivo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        Integer serial_number = jsonObject.getInt("serial_number");
        String nombre_dispositivo = jsonObject.getString("nombre_dispositivo");
        String tipo_dispositivo = jsonObject.getString("tipo_dispositivo");
        String rol_dispositivo = jsonObject.getString("rol_dispositivo");
        String pod_dispositivo = jsonObject.getString("pod_dispositivo");
        this.serviceDB.insertDispositivo(serial_number.toString(),nombre_dispositivo,tipo_dispositivo,rol_dispositivo);
        if (!pod_dispositivo.equals("No")) {
            this.serviceDB.insertAsignacionPodDispositivo(pod_dispositivo, serial_number.toString());
        }
    }

    @Override
    @RequestMapping(value = "/insertAsignacionAlumnoGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertAsignacionAlumnoGrupo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_alumno = jsonObject.getString("nombre_alumno");
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        if (nombre_grupo.equals("No")) {
            this.serviceDB.deleteAsignacionAlumnoGrupo(nombre_alumno);
        } else {
            if (this.serviceDB.getAsignacionAlumnoGrupo(nombre_alumno).isEmpty()) {
                this.serviceDB.insertAsignacionAlumnoGrupo(nombre_grupo, nombre_alumno);
            } else {
                this.serviceDB.deleteAsignacionAlumnoGrupo(nombre_alumno);
                this.serviceDB.insertAsignacionAlumnoGrupo(nombre_grupo, nombre_alumno);
            } 
        }
    }
    
    @Override
    @RequestMapping(value = "/insertAsignacionGrupoPod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertAsignacionGrupoPod(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_pod = jsonObject.getString("nombre_pod");
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        String nombre_rol = jsonObject.getString("nombre_rol");
        if (nombre_pod.equals("No")) {
            this.serviceDB.deleteAsignacionGrupoPodByGrupo(nombre_grupo);
        } else {
            if (this.serviceDB.getAsignacionGrupoPodByGrupo(nombre_grupo).isEmpty()) {
                this.serviceDB.insertAsignacionGrupoPod(nombre_grupo, nombre_pod, nombre_rol);;
            } else {
                this.serviceDB.deleteAsignacionGrupoPodByGrupo(nombre_grupo);
                this.serviceDB.insertAsignacionGrupoPod(nombre_grupo, nombre_pod, nombre_rol);;
            } 
        }
    }
    
    @Override
    @RequestMapping(value = "/insertLog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertLog(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        this.serviceDB.insertLog(jsonObject.getString("usuario_log"),jsonObject.getString("tipo"));
    }

    @Override
    @RequestMapping(value = "/insertDispositivoByXml", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertDispositivoByXml(@RequestParam("file")MultipartFile multipartFile) {
            try{    
                InputStream is = multipartFile.getInputStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();
                NodeList dispositivos = doc.getElementsByTagName("dispositivo");
                for (int i = 0; i < dispositivos.getLength(); i++) {
                    Node dispositivo = dispositivos.item(i);
                    if (dispositivo.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) dispositivo;
                        String serial_number = element.getElementsByTagName("serial_number").item(0).getTextContent();
                        String nombre_dispositivo = element.getElementsByTagName("nombre_dispositivo").item(0).getTextContent();
                        String tipo_dispositivo = element.getElementsByTagName("tipo_dispositivo").item(0).getTextContent();
                        String rol_dispositivo = element.getElementsByTagName("rol_dispositivo").item(0).getTextContent();
                        if(this.serviceDB.getDispositivoBySerial(serial_number).isEmpty()) {
                            this.serviceDB.insertDispositivo(serial_number,nombre_dispositivo,tipo_dispositivo,rol_dispositivo);
                        }
                    }
                }
            } catch (SAXException | IOException | ParserConfigurationException ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
    }
    
    // DELETES

    @Override
    @RequestMapping(value = "/deleteDispositivo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDispositivo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        Integer serial_number = jsonObject.getInt("serial_number");
        String nombre_dispositivo = jsonObject.getString("nombre_dispositivo");
        String tipo_dispositivo = jsonObject.getString("tipo_dispositivo");
        String rol_dispositivo = jsonObject.getString("rol_dispositivo");
        //DELETE RELACION DISPOSITIVO-POD
        this.serviceDB.deleteDispositivo(serial_number.toString(),nombre_dispositivo,tipo_dispositivo,rol_dispositivo);
    }
    
    @Override
    @RequestMapping(value = "/deleteGrupo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteGrupo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_grupo = jsonObject.getString("nombre_grupo");
        if(!this.serviceDB.getAsignacionGrupoPodByGrupo(nombre_grupo).isEmpty()) {
            this.serviceDB.deleteAsignacionGrupoPodByGrupo(nombre_grupo);
        }
        JSONArray alumnoGrupo = this.serviceDB.getAsignacionAlumnoGrupoByGrupo(nombre_grupo);
        if(!alumnoGrupo.isEmpty()) {
            for (Object object : alumnoGrupo) {
                JSONObject jsonLineItem = (JSONObject) object;
                this.serviceDB.deleteAsignacionAlumnoGrupo(jsonLineItem.getString("alumno"));
            }
        }
        this.serviceDB.deleteGrupo(nombre_grupo);
    }
    
    @Override
    @RequestMapping(value = "/deletePod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deletePod(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String nombre_pod = jsonObject.getString("nombre_pod");
        if(this.serviceDB.getAsignacionGrupoPodByPod(nombre_pod).isEmpty()) {
            this.serviceDB.deletePod(nombre_pod);
        } else {
            this.serviceDB.deleteAsignacionGrupoPodByPod(nombre_pod);
            this.serviceDB.deletePod(nombre_pod);
        }
    }

    @Override
    @RequestMapping(value = "/deleteReserva", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteReserva(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        String name = jsonObject.getString("name");
        String start = jsonObject.getString("start");
        String end = jsonObject.getString("end");
        String grupo = jsonObject.getString("grupo");
        String pod = jsonObject.getString("pod");
        String color = jsonObject.getString("color");
        this.serviceDB.deleteReserva(name,start,end,grupo,pod,color);
    }
    
    // UPDATES

    @Override
    @RequestMapping(value = "/updateDispositivo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateDispositivo(@RequestBody String string) {
        JSONObject jsonObject = new JSONObject(string);
        Integer serial_number = jsonObject.getInt("serial_number");
        String nombre_dispositivo = jsonObject.getString("nombre_dispositivo");
        String tipo_dispositivo = jsonObject.getString("tipo_dispositivo");
        String rol_dispositivo = jsonObject.getString("rol_dispositivo");
        String pod_dispositivo = jsonObject.getString("pod_dispositivo");
        
        if (this.serviceDB.getDispositivo(serial_number.toString(),nombre_dispositivo,tipo_dispositivo,rol_dispositivo).isEmpty()) {
            this.serviceDB.updateDispositivo(serial_number.toString(),nombre_dispositivo,tipo_dispositivo,rol_dispositivo);
        }
        if (pod_dispositivo.equals("No")) {
            this.serviceDB.deleteAsignacionPodDispositivo(serial_number.toString());
        } else {
            if (this.serviceDB.getAsignacionPodDispositivo(serial_number.toString()).isEmpty()) {
                this.serviceDB.insertAsignacionPodDispositivo(pod_dispositivo, serial_number.toString());
            } else {
                this.serviceDB.deleteAsignacionPodDispositivo(serial_number.toString());
                this.serviceDB.insertAsignacionPodDispositivo(pod_dispositivo, serial_number.toString());
            } 
        }
    }
}
