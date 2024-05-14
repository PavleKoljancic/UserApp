package com.example.userapp.models;

import java.io.Serializable;
import java.math.BigDecimal;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {

    private Integer id;
    private String pictureHash;
    private String email;
    private String firstName;
    private String lastName;
    private BigDecimal credit;

}
