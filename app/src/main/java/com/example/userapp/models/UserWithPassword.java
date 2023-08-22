package com.example.userapp.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UserWithPassword {

    private Integer Id;
    private byte[] PictureHash;
    private String DocumentName1;
    private String DocumentName2;
    private String DocumentName3;
    private String email;
    private String firstName;
    private String lastName;
    private BigDecimal Credit = new BigDecimal(0.0);
    private String passwordHash;
}
