package com.example.userapp.activity.main.fragments.buytickets;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.TicketType;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class TicketsApiDecorator extends UserApiDecorator {

    List<TicketType> getTickets() throws IOException {
        return  api.getTicketsInUse(0, 1000, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }

    String sendTicketRequest(TicketType ticketType) throws JSONException, IOException {
        return  api.addTicketRequest(ticketType.getId(), TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }


}
