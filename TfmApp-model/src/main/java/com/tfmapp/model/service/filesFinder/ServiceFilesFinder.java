package com.tfmapp.model.service.filesFinder;

import java.util.ArrayList;

import org.json.JSONArray;

import com.tfmapp.model.repository.data.files_directory.Directory;

public interface ServiceFilesFinder {

    public ArrayList<Directory> searchFiles();
    
    public ArrayList<String> getDownloadDocuments(JSONArray jsonArray);
    
}
