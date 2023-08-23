package com.example.userapp.models;

import java.math.BigDecimal;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode


public class TicketType {

    private String type;
    Integer validFor;
    Integer amount;
    private Integer id;
    private String name;
    private BigDecimal cost;
    private String documentaionName;
    private Boolean needsDocumentaion;
    private Boolean inUse;
}
