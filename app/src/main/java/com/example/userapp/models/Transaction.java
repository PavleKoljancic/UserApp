package com.example.userapp.models;

import com.example.userapp.activity.main.fragments.interactions.InteractionsFragment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {


    private Integer Id;
    private BigDecimal amount;
    private Timestamp timestamp;
    private Integer userId;

    private Integer terminalId;

    private Integer supervisorId;

    private Integer ticketRequestResponseId;

}
