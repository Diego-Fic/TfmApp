package com.tfmapp.model.repository.data.files_directory;

import java.util.Date;

import org.springframework.stereotype.Repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Repository
public class Document {
    
    private String name;
    private String modification_date;

}
