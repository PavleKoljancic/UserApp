package com.example.userapp.datamodel.requests;

import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;

import java.util.ArrayList;
import java.util.List;

public class RequestDataModel {

    private List<TicketType> allTickets;
    private List<TicketRequest> userTicketRequest;


    private List<TicketRequestResponse> userTicketResponses;

    private static RequestDataModel instance=null;



    private  boolean responseSelected;

    private  RequestDataModel()
    {
        this.userTicketRequest= new ArrayList<>();
        this.userTicketResponses= new ArrayList<>();
        this.allTickets= new ArrayList<>();
        this.responseSelected =true;
    }
    public boolean isResponseSelected() {
        return responseSelected;
    }

    public void setResponseSelected(boolean responseSelected) {
        this.responseSelected = responseSelected;
    }
    public static RequestDataModel getInstance()
    {
        if(instance==null)
            instance = new RequestDataModel();
        return  instance;
    }

    public List<TicketType> getAllTickets() {
        return allTickets;
    }

    public void setAllTickets(List<TicketType> allTickets) {
        if(allTickets!=null&&allTickets.size()>0)
        {
            this.allTickets.clear();
            this.allTickets.addAll(allTickets);
        }
    }

    public List<TicketRequest> getUserTicketRequest() {
        return userTicketRequest;
    }

    public void setUserTicketRequest(List<TicketRequest> userTicketRequest) {
        if(userTicketRequest!=null&&userTicketRequest.size()>0)
        {
            this.userTicketRequest.clear();
            this.userTicketRequest.addAll(userTicketRequest);
        }
    }

    public List<TicketRequestResponse> getUserTicketResponses() {
        return userTicketResponses;
    }

    public void setUserTicketResponses(List<TicketRequestResponse> userTicketResponses) {
        if(userTicketResponses!=null&&userTicketResponses.size()>0)
        {
            this.userTicketResponses.clear();
            this.userTicketResponses.addAll(userTicketResponses);
        }
    }

}
