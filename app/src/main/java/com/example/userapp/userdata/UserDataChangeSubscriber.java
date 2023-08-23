package com.example.userapp.userdata;

import android.graphics.Bitmap;

import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;

import java.util.HashSet;

public interface UserDataChangeSubscriber {

    void onUserDataChanged(
            User user,
            HashSet<UserTicket> userTickets,
            HashSet<TicketRequestResponse> ticketRequestResponses,
            HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture
    );
}
