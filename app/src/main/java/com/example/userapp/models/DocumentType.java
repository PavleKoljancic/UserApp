package com.example.userapp.models;


import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentType implements Serializable {

    Integer id;
    String name;
    Date validFromDate;
    Date validUntilDate;

    @Override
    public String toString()
    {
        return this.name;
    }
}
