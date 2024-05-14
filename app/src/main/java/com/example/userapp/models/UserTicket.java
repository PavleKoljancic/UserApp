package com.example.userapp.models;

import java.io.Serializable;
import java.sql.Timestamp;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserTicket implements Serializable {

    private Integer transaction_Id;
    private Timestamp validUntilDate;
    private Integer usage;
    private TicketType type;
}
