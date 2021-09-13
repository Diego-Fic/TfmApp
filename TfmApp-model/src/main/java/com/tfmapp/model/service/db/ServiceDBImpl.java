package com.tfmapp.model.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mysql.cj.xdevapi.JsonArray;

@Service
public class ServiceDBImpl implements ServiceDB {

    static final String DB_URL = "jdbc:mysql://localhost:3306/asignatura_master";
    static final String USER = "root";
    static final String PASS = "root";
    static final String QUERY_ALUMNOS = "SELECT nombre_alumno FROM alumno";
    static final String QUERY_CONEXIONES = "SELECT dispositivo, puerto FROM conexiones";
    static final String QUERY_CONEXIONES_BY_GRUPO = "SELECT c.dispositivo,c.puerto FROM conexiones c join asignacion_pod_dispositivo apd on c.dispositivo = apd.dispositivo join asignacion_grupo_pod agp on agp.grupo = ";
    static final String QUERY_ALUMNOS_BY_NAME = "SELECT nombre_alumno FROM alumno where nombre_alumno = ";
    static final String QUERY_GRUPOS = "SELECT nombre_grupo FROM grupos";
    static final String QUERY_PODS = "SELECT nombre_pod FROM pods";
    static final String QUERY_DISPOSITIVOS = "SELECT serial_number, nombre_dispositivo, tipo_dispositivo, rol_dispositivo FROM dispositivos";
    static final String QUERY_ASIGNACION_ALUMNO_GRUPO = "SELECT grupo FROM asignacion_alumno_grupo where alumno = ";
    static final String QUERY_ASIGNACION_ALUMNO_GRUPO_BY_GRUPO = "SELECT alumno, grupo FROM asignacion_alumno_grupo where grupo = ";
    static final String QUERY_RESERVA_BY_GRUPO = "SELECT grupo,pod,fecha_ini,fecha_fin,nombre,color FROM reserva where grupo = ";
    static final String QUERY_ASIGNACION_GRUPO_POD = "SELECT pod,rol FROM asignacion_grupo_pod where grupo = ";
    static final String QUERY_ASIGNACION_POD_GRUPO = "SELECT pod,rol FROM asignacion_grupo_pod where pod = ";
    static final String QUERY_ASIGNACION_POD_DISPOSITIVO = "SELECT pod,dispositivo,fecha FROM asignacion_pod_dispositivo  where dispositivo = ";
    static final String QUERY_LATENCIA = "(select ROUND(AVG(latencia)) as latencia,month(fecha) as mes from latencia group by year(fecha),month(fecha) order by mes DESC LIMIT 6) order by mes asc";
    static final String QUERY_LOGS_POR_MES = "SELECT count(*) as conexiones,mes FROM (SELECT month(fecha) as mes FROM LOGS GROUP BY year(fecha),month(fecha),usuario_log) t group by mes order by mes asc limit 4";
    static final String QUERY_TOTAL_LOGS_POR_MES = "SELECT COUNT(*) AS total,month(fecha) as mes FROM LOGS WHERE TIPO LIKE \"INICIO_SESION\" GROUP BY year(fecha),month(fecha) order by mes asc limit 4";

    @Override
    public Boolean connectDatabase() {
        boolean valid = false;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            Connection connection = null;
            // Database connect
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            valid = connection.isValid(50000);
        } catch (SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
        return valid;
    }

    // SELECTS
    
    @Override
    public JSONArray getConexiones() {
        // Open a connection
        JSONArray executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_CONEXIONES);
                executedQuery = convertToJSONArray(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONArray getConexionesByGrupo(String grupo) {
        // Open a connection
        JSONArray executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_CONEXIONES_BY_GRUPO + "\"" + grupo + "\"");
                executedQuery = convertToJSONArray(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONArray getAlumnos() {
        // Open a connection
        JSONArray executedQuery = null;
        JSONArray resultQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ALUMNOS);
                executedQuery = convertToJSONArray(rs);
                resultQuery = buscarAsignacionAlumnoGrupo(executedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultQuery;
    }
    
    @Override
    public JSONArray getGrupos() {
        // Open a connection
        JSONArray executedQuery = null;
        JSONArray resultQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_GRUPOS);
                executedQuery = convertToJSONArray(rs);
                resultQuery = buscarAsignacionGrupoPod(executedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultQuery;
    }

    @Override
    public JSONArray getPods() {
        // Open a connection
        JSONArray executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_PODS);
                executedQuery = convertToJSONArray(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }

    @Override
    public JSONArray getDispositivos() {
        // Open a connection
        JSONArray executedQuery = null;
        JSONArray resultQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_DISPOSITIVOS);
                executedQuery = convertToJSONArray(rs);
                resultQuery = buscarAsignacionPodDispositivo(executedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultQuery;
    }
    
    
    @Override
    public JSONObject getDispositivoBySerial(String serial_number) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT serial_number,nombre_dispositivo,tipo_dispositivo,rol_dispositivo FROM dispositivos where serial_number = \"" + serial_number + "\"";
                ResultSet rs = stmt.executeQuery(sql);
                executedQuery = convertToJSONObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONObject getDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo, String rol_dispositivo) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "SELECT serial_number,nombre_dispositivo,tipo_dispositivo,rol_dispositivo FROM dispositivos where serial_number = " + serial_number 
                        + " AND nombre_dispositivo = \"" + nombre_dispositivo 
                        + "\" AND tipo_dispositivo = \"" + tipo_dispositivo 
                        + "\" AND rol_dispositivo = \"" + rol_dispositivo + "\"";
                ResultSet rs = stmt.executeQuery(sql);
                executedQuery = convertToJSONObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONObject getAlumnoByName(String nombre_alumno) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ALUMNOS_BY_NAME + "\"" + nombre_alumno + "\"");
                executedQuery = convertToJSONObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONObject getAsignacionPodDispositivo(String serial_number) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ASIGNACION_POD_DISPOSITIVO + serial_number);
                executedQuery = convertToJSONObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }

    @Override
    public JSONObject getAsignacionGrupoPodByGrupo(String grupo) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ASIGNACION_GRUPO_POD + "\"" + grupo + "\"");
                executedQuery = convertToMultipleJSONObject(rs,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }

    @Override
    public JSONObject getAsignacionGrupoPodByPod(String pod) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ASIGNACION_POD_GRUPO + "\"" + pod + "\"");
                executedQuery = convertToMultipleJSONObject(rs,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONObject getAsignacionAlumnoGrupo(String nombre_alumno) {
        // Open a connection
        JSONObject executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ASIGNACION_ALUMNO_GRUPO + "\"" + nombre_alumno + "\"");
                executedQuery = convertToJSONObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONArray getAsignacionAlumnoGrupoByGrupo(String nombre_grupo) {
        // Open a connection
        JSONArray executedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_ASIGNACION_ALUMNO_GRUPO_BY_GRUPO + "\"" + nombre_grupo + "\"");
                executedQuery = convertToJSONArray(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executedQuery;
    }
    
    @Override
    public JSONArray getReservasByGrupo(String nombre_grupo) {
        // Open a connection
        JSONArray executedQuery = null;
        JSONArray formatedQuery = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_RESERVA_BY_GRUPO + "\"" + nombre_grupo + "\"");
                executedQuery = convertToJSONArray(rs);
                formatedQuery = formatDates(executedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedQuery;
    }

    @Override
    public JSONArray getMediaLatencias() {
        // Open a connection
        JSONArray executedQuery = new JSONArray();
        JSONArray jsonArray = new JSONArray();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_LATENCIA);
                executedQuery = convertToJSONArray(rs);
                jsonArray = convertirMes(executedQuery,"latencia");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @Override
    public JSONArray getConexionesMes() {
        // Open a connection
        JSONArray executedQuery = new JSONArray();
        JSONArray executedQueryTotal = new JSONArray();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_LOGS_POR_MES);
                executedQuery = convertToJSONArray(rs);
                jsonArray = convertirMes(executedQuery,"conexiones");
                ResultSet rs2 = stmt.executeQuery(QUERY_TOTAL_LOGS_POR_MES);
                executedQueryTotal = convertToJSONArray(rs2);
                jsonArray2 = convertirMes(executedQueryTotal,"total");
                jsonArray3 = fusionarArrays(jsonArray, jsonArray2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray3;
    }
    // INSERTS
    
    @Override
    public void insertAlumnos(JSONArray alumnosLdap) {
        for (Object object : alumnosLdap) {
            JSONObject jsonObjectAlumnoBD = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            jsonObjectAlumnoBD = getAlumnoByName(jsonLineItem.getString("nombre_alumno_ldap"));
            if (jsonObjectAlumnoBD.isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                        Statement stmt = conn.createStatement();
                        String sql = "INSERT INTO alumno (nombre_alumno) VALUES (\"" + jsonLineItem.getString("nombre_alumno_ldap") + "\")";
                        stmt.executeUpdate(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void insertReserva(String grupo, String pod, String fecha_ini, String fecha_fin, String nombre,String color) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO reserva (grupo,pod,fecha_ini,fecha_fin,nombre,color) VALUES (\"" + grupo + "\",\"" + pod
                    + "\",\"" + fecha_ini + "\",\"" + fecha_fin + "\",\"" + nombre + "\",\"" + color + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insertAsignacionGrupoPod(String grupo, String pod, String rol) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO asignacion_grupo_pod (pod,grupo,fecha,rol) VALUES (\"" + pod + "\",\"" + grupo
                    + "\",\"" + getDate() + "\",\"" + rol + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertAsignacionAlumnoGrupo(String grupo, String pod, String rol) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO asignacion_grupo_pod (pod,grupo,fecha,rol) VALUES (\"" + pod + "\",\"" + grupo
                    + "\",\"" + getDate() + "\",\"" + rol + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insertAsignacionAlumnoGrupo(String grupo, String alumno) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO asignacion_alumno_grupo (grupo,alumno) VALUES (\"" + grupo + "\",\"" + alumno
                    + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertLog(String usuario_log, String tipo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO logs (usuario_log,fecha,tipo) VALUES (\"" + usuario_log + "\",\"" + getDate()
                    + "\",\"" + tipo + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertAsignacionPodDispositivo(String pod, String serial_number) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO asignacion_pod_dispositivo (pod,dispositivo,fecha) VALUES (\"" + pod + "\",\""
                    + serial_number + "\",\"" + getDate() + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insertDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo,
            String rol_dispositivo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO dispositivos (serial_number,nombre_dispositivo,tipo_dispositivo,rol_dispositivo) VALUES (\"" + serial_number + "\",\""
                    + nombre_dispositivo + "\",\"" + tipo_dispositivo + "\",\"" + rol_dispositivo + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insertGrupo(String nombre_grupo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO grupos (nombre_grupo) VALUES (\"" + nombre_grupo + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insertPods(String nombre_pod) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO pods (nombre_pod) VALUES (\"" + nombre_pod + "\")";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // DELETES
    @Override
    public void deleteDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo,
            String rol_dispositivo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM dispositivos WHERE serial_number = \"" + serial_number
                    + "\" AND nombre_dispositivo = " + "\"" + nombre_dispositivo + "\" AND tipo_dispositivo = \""
                    + tipo_dispositivo + "\" AND rol_dispositivo = \"" + rol_dispositivo + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAsignacionPodDispositivo(String serial_number) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM asignacion_pod_dispositivo WHERE dispositivo = " + "\"" + serial_number + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteAsignacionGrupoPodByGrupo(String grupo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM asignacion_grupo_pod WHERE grupo = " + "\"" + grupo + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteAsignacionGrupoPodByPod(String pod) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM asignacion_grupo_pod WHERE pod = " + "\"" + pod + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteAsignacionAlumnoGrupo(String nombre_alumno) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM asignacion_alumno_grupo WHERE alumno = " + "\"" + nombre_alumno + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteGrupo(String nombre_grupo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM grupos WHERE nombre_grupo = \"" + nombre_grupo + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deletePod(String nombre_pod) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM pods WHERE nombre_pod = \"" + nombre_pod + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteReserva(String name,String start,String end,String grupo,String pod,String color) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM reserva WHERE grupo = \"" + grupo + "\"" + " AND pod =  \"" + pod + "\"" + " AND fecha_ini = \"" + start + "\"" + " AND fecha_fin = \"" + end + "\"" + " AND color = \"" + color + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // UPDATES

    @Override
    public void updateDispositivo(String serial_number, String nombre_dispositivo, String tipo_dispositivo,
            String rol_dispositivo) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "UPDATE dispositivos SET nombre_dispositivo = " + "\"" + nombre_dispositivo + "\", tipo_dispositivo = \""
                    + tipo_dispositivo + "\", rol_dispositivo = \"" + rol_dispositivo + "\" WHERE serial_number = " + serial_number;
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateAlumnos(String nombre_pod) {
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                Statement stmt = conn.createStatement();
                String sql = "DELETE FROM pods WHERE nombre_pod = \"" + nombre_pod + "\"";
                stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // FUNCIONES AUXILIARES
    
    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static JSONObject convertToJSONObject(ResultSet resultSet) throws Exception {
        JSONObject obj = new JSONObject();
        if (resultSet.next()) {
            obj.put(resultSet.getMetaData().getColumnLabel(1).toLowerCase(), resultSet.getObject(1));
        }
        return obj;
    }
    
    private static JSONObject convertToMultipleJSONObject(ResultSet resultSet, Integer variables) throws Exception {
        JSONObject obj = new JSONObject();
        if (resultSet.next()) {
            for (int i = 1; i<= variables; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
            }
        }
        return obj;
    }
    
    private static JSONArray convertToJSONArray(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject obj = new JSONObject();
            int total_rows = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }
    
    private JSONArray convertirMes(JSONArray executedQuery,String tipo) {
        JSONArray jsonArray = new JSONArray();
        for (Object object : executedQuery) {
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            nuevoObj.put(tipo,jsonLineItem.getInt(tipo));
            nuevoObj.put("mes",mesToString(jsonLineItem.getInt("mes")));
            jsonArray.put(nuevoObj);
        }
        return jsonArray;
    }
    
    private String mesToString (Integer numeroMes) {
        
        switch (numeroMes) {
        case 1:
            return "Enero";
        case 2:
            return "Febrero";
        case 3:
            return "Marzo";
        case 4:
            return "Abril";
        case 5:
            return "Mayo";
        case 6:
            return "Junio";
        case 7:
            return "Julio";
        case 8:
            return "Agosto";
        case 9:
            return "Septiembre";
        case 10:
            return "Octubre";
        case 11:
            return "Noviembre";
        case 12:
            return "Diciembre";
        }
        return null;
    }

    private JSONArray fusionarArrays(JSONArray a, JSONArray b) {
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < a.length(); j++) {
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) a.get(j);
            JSONObject jsonLineItem2 = (JSONObject) b.get(j);
            nuevoObj.put("conexiones",jsonLineItem.getInt("conexiones"));
            nuevoObj.put("mes",jsonLineItem.getString("mes"));
            nuevoObj.put("total",jsonLineItem2.getInt("total"));
            jsonArray.put(nuevoObj);
        }
        return jsonArray;
    }

    private JSONArray buscarAsignacionPodDispositivo(JSONArray executedQuery) {
        JSONArray jsonArrayResult = new JSONArray();
        for (Object object : executedQuery) {
            JSONObject jsonObjectAsignacionPodDispositivo = new JSONObject();
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            Integer serial_number = jsonLineItem.getInt("serial_number");
            nuevoObj.put("serial_number",serial_number.toString());
            jsonObjectAsignacionPodDispositivo = getAsignacionPodDispositivo(serial_number.toString());
            nuevoObj.put("nombre_dispositivo",jsonLineItem.getString("nombre_dispositivo"));
            nuevoObj.put("tipo_dispositivo",jsonLineItem.getString("tipo_dispositivo"));
            nuevoObj.put("rol_dispositivo",jsonLineItem.getString("rol_dispositivo"));
            nuevoObj.put("pod_dispositivo",jsonObjectAsignacionPodDispositivo.isEmpty() ? "No" : jsonObjectAsignacionPodDispositivo.getString("pod"));
            jsonArrayResult.put(nuevoObj);
        }
        return jsonArrayResult;
    }

    private JSONArray buscarAsignacionAlumnoGrupo(JSONArray executedQuery) {
        JSONArray jsonArrayResult = new JSONArray();
        for (Object object : executedQuery) {
            JSONObject jsonObjectAsignacionAlumnoGrupo = new JSONObject();
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            nuevoObj.put("nombre_alumno",jsonLineItem.getString("nombre_alumno"));
            jsonObjectAsignacionAlumnoGrupo = getAsignacionAlumnoGrupo(jsonLineItem.getString("nombre_alumno"));
            nuevoObj.put("nombre_grupo",jsonObjectAsignacionAlumnoGrupo.isEmpty() ? "No" : jsonObjectAsignacionAlumnoGrupo.getString("grupo"));
            jsonArrayResult.put(nuevoObj);
        }
        return jsonArrayResult;
    }
    
    private JSONArray buscarAsignacionGrupoPod(JSONArray executedQuery) {
        JSONArray jsonArrayResult = new JSONArray();
        for (Object object : executedQuery) {
            JSONObject jsonObjectAsignacionAlumnoGrupo = new JSONObject();
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            nuevoObj.put("nombre_grupo",jsonLineItem.getString("nombre_grupo"));
            jsonObjectAsignacionAlumnoGrupo = getAsignacionGrupoPodByGrupo(jsonLineItem.getString("nombre_grupo"));
            nuevoObj.put("nombre_rol",jsonObjectAsignacionAlumnoGrupo.isEmpty() ? "INACTIVO" : jsonObjectAsignacionAlumnoGrupo.getString("rol"));
            nuevoObj.put("nombre_pod",jsonObjectAsignacionAlumnoGrupo.isEmpty() ? "No" : jsonObjectAsignacionAlumnoGrupo.getString("pod"));
            jsonArrayResult.put(nuevoObj);
        }
        return jsonArrayResult;
    }

    
    private JSONArray formatDates (JSONArray executedQuery) {
        JSONArray jsonArrayResult = new JSONArray();
        for (Object object : executedQuery) {
            JSONObject nuevoObj = new JSONObject();
            JSONObject jsonLineItem = (JSONObject) object;
            nuevoObj.put("grupo",jsonLineItem.getString("grupo"));
            nuevoObj.put("pod",jsonLineItem.getString("pod"));
            nuevoObj.put("color",jsonLineItem.getString("color"));
            nuevoObj.put("nombre",jsonLineItem.getString("nombre"));

            String fecha_ini = jsonLineItem.get("fecha_ini").toString();
            String[] subs_ini = fecha_ini.split("T");
            String fecha_fin = jsonLineItem.get("fecha_fin").toString();
            String[] subs_fin = fecha_fin.split("T");
            
            nuevoObj.put("fecha_ini", subs_ini[0] + " " + subs_ini[1] + ":00");
            nuevoObj.put("fecha_fin",subs_fin[0] + " " + subs_fin[1] + ":00");

            jsonArrayResult.put(nuevoObj);
        }
        return jsonArrayResult;
    }
    
    
}
