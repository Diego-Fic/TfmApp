package com.tfmapp.model.service.db;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ServiceDB {

    public Boolean connectDatabase();
    
    // SELECTS
    public JSONArray getConexiones();
    
    public JSONArray getConexionesByGrupo(String grupo);
    
    public JSONArray getAlumnos();
    
    public JSONObject getAlumnoByName(String nombre_alumno);
    
    public JSONArray getGrupos();
    
    public JSONArray getPods();
    
    public JSONArray getDispositivos();

    public JSONObject getDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo, String rol_dispositivo);

    public JSONObject getDispositivoBySerial(String serial_number);

    public JSONObject getAsignacionAlumnoGrupo(String nombre_alumno);

    public JSONArray getAsignacionAlumnoGrupoByGrupo(String nombre_grupo);
    
    public JSONObject getAsignacionPodDispositivo(String serial_number);
    
    public JSONObject getAsignacionGrupoPodByGrupo(String grupo);
            
    public JSONObject getAsignacionGrupoPodByPod(String pod);
    
    public JSONArray getMediaLatencias();
    
    public JSONArray getConexionesMes();
    
    public JSONArray getReservasByGrupo(String nombre_grupo);
    
    // INSERTS
    public void insertReserva(String grupo, String pod, String fecha_ini, String fecha_fin, String nombre,String color);
    
    public void insertAlumnos(JSONArray alumnosLdap);

    public void insertGrupo(String nombre_grupo);
    
    public void insertPods(String nombre_pod);
    
    public void insertDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo, String rol_dispositivo);
    
    public void insertAsignacionAlumnoGrupo(String grupo, String alumno);
    
    public void insertAsignacionGrupoPod(String grupo, String pod, String rol);
        
    public void insertAsignacionPodDispositivo(String pod, String serial_number);
    
    public void insertLog(String usuario_log, String tipo);

    public void insertAsignacionAlumnoGrupo(String grupo, String pod, String rol);
    
    // DELETES
    
    public void deleteDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo, String rol_dispositivo);
    
    public void deleteAsignacionPodDispositivo(String serial_number);
    
    public void deleteAsignacionGrupoPodByGrupo(String grupo);
    
    public void deleteAsignacionGrupoPodByPod(String pod);
    
    public void deleteAsignacionAlumnoGrupo(String nombre_alumno);
    
    public void deleteGrupo(String nombre_grupo);
    
    public void deletePod(String nombre_pod);

    public void deleteReserva(String name,String start,String end,String grupo,String pod,String color);
    
    // UPDATES

    public void updateDispositivo(String string, String nombre_dispositivo, String tipo_dispositivo,
            String rol_dispositivo);
    
    public void updateAlumnos(String nombre_pod);
    
}
