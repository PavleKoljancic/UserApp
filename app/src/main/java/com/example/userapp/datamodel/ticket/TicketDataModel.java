package com.example.userapp.datamodel.ticket;

import com.example.userapp.datamodel.CacheLayer;
import com.example.userapp.models.TicketType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TicketDataModel {

    private List<TicketType> tickets;

    private static   TicketDataModel instance=null;

    private  TicketDataModel()
    {
        tickets = new ArrayList<TicketType>();
    }

    public static TicketDataModel getInstance() {
        if(instance==null)
            instance =new TicketDataModel();
        return instance;
    }

    public List<TicketType> getTickets() {
        return tickets;
    }
    public void setTickets(Collection<TicketType> newTickets)
    {
        if(newTickets!=null) {
            this.tickets.clear();
            this.tickets.addAll(newTickets);
            CacheLayer cacheLayer = CacheLayer.getInstance();
            cacheLayer.writeObject(this.tickets,"ticketTYPES");
        }
    }

    public void loadFromFile()
    {

        CacheLayer cacheLayer = CacheLayer.getInstance();
        List<TicketType> tempTickets = (List<TicketType>) cacheLayer.readObject("ticketTYPES");
        if(tempTickets!=null)
        {
            this.tickets.clear();
            this.tickets.addAll(tempTickets);
        }
    }
}
