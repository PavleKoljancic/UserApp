package com.example.userapp.models;

import java.sql.Timestamp;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserTicket {

    private Timestamp validUntilDate;
    private Integer usage;
    private TicketType type;
}
