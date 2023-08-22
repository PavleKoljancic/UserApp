package com.example.userapp.models;



import java.sql.Timestamp;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class TicketRequest {

    Integer id;
    Timestamp dateTime;

    Integer userId ;
    Integer ticketTypeId ;
}
