package com.example.userapp.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketRequestResponse {
    Integer id;

    String comment;
    Integer ticketRequestId;
    Boolean approved;
    Integer supervisorId;
}
