package com.example.userapp.models;

import java.math.BigDecimal;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@Getter
@Setter
@NoArgsConstructor



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
