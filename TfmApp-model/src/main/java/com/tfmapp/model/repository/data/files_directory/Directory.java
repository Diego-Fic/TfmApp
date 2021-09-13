package com.tfmapp.model.repository.data.files_directory;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Repository
public class Directory {
    
    private String name;
    private ArrayList<Document> file_list;
}
