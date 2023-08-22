package com.example.userapp.models;

import java.io.Serializable;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class AcceptedPrimaryKey implements Serializable {

    public Integer transporterId;

    public Integer ticketTypeId;

    public AcceptedPrimaryKey(Integer TransporterId, Integer TicketTypeId) 
    {
        this.ticketTypeId = TicketTypeId;
        this.transporterId = TransporterId;
    }
}