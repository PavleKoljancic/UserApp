package com.example.userapp.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScanInteraction implements Serializable {

    ScanInterractionPrimaryKey id;
    private Integer transactionId;
}
