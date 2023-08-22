package com.example.userapp.models;

import java.math.BigDecimal;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class User {

    private Integer id;
    private byte [] pictureHash;
    private String documentName1;
    private String documentName2;
    private String documentName3;
    private String email;
    private String firstName;
    private String lastName;
    private BigDecimal credit;

}
