package com.example.userapp.activity.main.fragments.ticketrequests;

import com.example.userapp.activity.ActivityModel;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;
import com.example.userapp.singeltons.TokenManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class RequestsFragmentModel extends ActivityModel {


    List<TicketRequest> getUserTicketRequests() throws JSONException, IOException {

        return api.getUnprocessTicketRequests(TokenManager.getInstance().getId(), 0, 1000, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }
    List<TicketRequestResponse> getUserTicketResponses() throws JSONException, IOException {

        return api.getTicketResponseByUserId(TokenManager.getInstance().getId(), 0, 1000, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }

    List<TicketType> getAllTicketTypes() throws  IOException {

        return api.getAllTicketTypes(0, 1000, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }
}
