package com.tfmapp.model.service.filesFinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import com.tfmapp.model.repository.data.files_directory.Directory;
import com.tfmapp.model.repository.data.files_directory.Document;

@Service
public class ServiceFilesFinderImpl implements ServiceFilesFinder {
    
    public ArrayList<String> getDownloadDocuments(JSONArray jsonArray){
        
        ArrayList<String> localizacion_directorios = new ArrayList<String>();
        
        ArrayList<String> path = parseLocation();
        StringBuilder sb = new StringBuilder();
        for (String s : path) {
            sb.append(s);
            sb.append("/");
        }

        String final_path = sb.toString();
        for(Object o: jsonArray){
            if ( o instanceof JSONObject ) {
                String temario = ((JSONObject) o).get("temario").toString();
                String nombre = ((JSONObject) o).get("nombre").toString();
                String ruta = final_path.concat(temario).concat("/").concat(nombre);
                localizacion_directorios.add(ruta);
            }
        }
        
        return localizacion_directorios;

    }

    public ArrayList<Directory> searchFiles() {

        ArrayList<String> path = parseLocation();
        StringBuilder sb = new StringBuilder();
        for (String s : path) {
            sb.append(s);
            sb.append("/");
        }

        String final_path = sb.toString();

        ArrayList<Directory> lista_directorios = parseRootFiles(path, final_path);
        
        return lista_directorios;
    }

    private  ArrayList<Directory> parseRootFiles(ArrayList<String> path, String final_path) {

        ArrayList<Directory> lista_directorios = new ArrayList<Directory>();
        File f = new File(final_path);
        File[] root_files = f.listFiles();

        for (File file : root_files) {
            String filename = getFileName(file.toString());
            lista_directorios.add(parseRootLocation(file.toString(), filename));
        }

        return lista_directorios;
    }

    private String getFileName(String rootFile) {

        String[] splitPath = rootFile.split("\\\\");
        return splitPath[splitPath.length - 1];

    }

    private Directory parseRootLocation(String path, String rootFile) {

        Directory directory = new Directory();
        directory.setName(rootFile);

        ArrayList<Document> listaFicheros = new ArrayList<Document>();

        File f = new File(path);
        File[] root_files = f.listFiles();

        for (File file : root_files) {
            Document document = new Document();
            document.setName(getFileName(file.toString()));
            document.setModification_date(getModificationDate(file.toString()));
            listaFicheros.add(document);
        }
        directory.setFile_list(listaFicheros);
        return directory;
    }

    private String getModificationDate(String file) {

        Path file_source = Paths.get(file);
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(file_source, BasicFileAttributes.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Calendar atime = Calendar.getInstance();
        atime.setTimeInMillis(attr.lastAccessTime().toMillis());
        Date atimeAsDate = atime.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(atimeAsDate);
        return today;
    }

    private ArrayList<String> parseLocation() {

        String path = System.getProperty("user.dir");
        String[] splitPath = path.split("\\\\");

        ArrayList<String> result = removeLastValuePath(splitPath);

        return result;
    }

    private ArrayList<String> removeLastValuePath(String[] splitPath) {
        ArrayList<String> result = new ArrayList<String>();
        String dirFilesPath = "FrontEnd/cliente-web/src/assets/db";

        for (int i = 0; i < splitPath.length - 1; i++) {
            result.add(splitPath[i]);
        }
        result.add(dirFilesPath);
        return result;

    }

}
