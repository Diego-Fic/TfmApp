package com.tfmapp.ws.controller.db;

import javax.naming.NamingException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface DBRestController {
    
    // SELECTS
    public String getConexiones() throws NamingException;
    
    public String getConexionesByGrupo(@RequestBody String string) throws NamingException;
    
    public String getAsignacionGrupoPod(@RequestBody String string);
    
    public void getAlumnosLdap() throws NamingException;
    
    public String getAlumnos();
    
    public String getGrupos();
    
    public String getPods();
    
    public String getDispositivos();
    
    public String getMediaLatencias();
    
    public String getConexionesPorMes();
    
    public String getAsignacionAlumnoGrupo(@RequestBody String string);
    
    public String getReservasByGrupo(@RequestBody String string);
    
    // INSERTS
    
    public void insertReserva(@RequestBody String string);
    
    public void insertGrupos(@RequestBody String string);
    
    public void insertPods(@RequestBody String string);
    
    public void insertAsignacionAlumnoGrupo(@RequestBody String string);
    
    public void insertLog(@RequestBody String string);
    
    public void insertDispositivo(@RequestBody String string);

    public void insertAsignacionGrupoPod(@RequestBody String string);
    
    public void insertDispositivoByXml(@RequestParam("file")MultipartFile multipartFile);

    // DELETES
    public void deleteDispositivo(@RequestBody String string);
    
    public void deleteGrupo(@RequestBody String string);
    
    public void deletePod(@RequestBody String string);
    
    public void deleteReserva(@RequestBody String string);
    
    // UPDATES
    
    public void updateDispositivo(@RequestBody String string);


}
