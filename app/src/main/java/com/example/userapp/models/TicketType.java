package com.example.userapp.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode


public class TicketType implements Serializable {

    private String type;
    Integer validFor;
    Integer amount;
    private Integer id;
    private String name;
    private BigDecimal cost;
    private Boolean needsDocumentaion;
    private Boolean inUse;

    private List<DocumentType> documents ;
}
