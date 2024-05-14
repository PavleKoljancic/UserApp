package com.example.userapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Route implements Serializable {

    private Integer id;
    private String name;
    private  Boolean isActive = true;
    private Integer transporterId;
}
