package com.example.userapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Document implements Serializable {

    Integer id;
    Integer userId;
    private DocumentType documentType;
    Boolean approved;
    String comment;
    Integer supervisorId;
    

}
