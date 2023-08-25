package com.example.userapp.datamodel.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.nfc.UserAppHceService;

import java.util.HashSet;
import java.util.List;

import lombok.Getter;

@Getter
public class UserDataModel {
    private User user;
    private HashSet<UserTicket> userTickets;
    private Bitmap userProfilePicture;
    private HashSet<TicketRequestResponse> ticketRequestResponses;
    private HashSet<TicketRequest> unprocessedTicketRequest;

    private HashSet<UserDataChangeSubscriber> dataChangeSubscribers;
    private static UserDataModel userDataModel = null;

    private UserDataModel() {
        this.user = null;
        this.userTickets = new HashSet<UserTicket>();
        this.ticketRequestResponses = new HashSet<TicketRequestResponse>();
        this.unprocessedTicketRequest = new HashSet<TicketRequest>();
        this.dataChangeSubscribers = new HashSet<UserDataChangeSubscriber>();
        this.userProfilePicture = null;
    }

    public static UserDataModel getInstance() {
        if (userDataModel == null)
            userDataModel = new UserDataModel();
        return userDataModel;
    }

    public boolean subscribeToDataChange(UserDataChangeSubscriber subscriber) {
        synchronized (this.dataChangeSubscribers) {
            return this.dataChangeSubscribers.add(subscriber);
        }
    }

    public boolean unsubscribeToDataChange(UserDataChangeSubscriber subscriber) {
        synchronized (this.dataChangeSubscribers) {
            return this.dataChangeSubscribers.remove(subscriber);
        }
    }

    private void informSubscribers(User user, HashSet<UserTicket> userTickets, HashSet<TicketRequestResponse> ticketRequestResponses, HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture) {
        synchronized (dataChangeSubscribers) {
            for (UserDataChangeSubscriber subscriber : dataChangeSubscribers)
                if (subscriber != null)
                    subscriber.onUserDataChanged(user, userTickets, ticketRequestResponses, unprocessedTicketRequest, userProfilePicture);
        }
    }

    public void updateUser(User user) {
        Boolean dataChanged = false;
        if (this.user == null) {
            if (user != null) {
                this.user = user;
                UserAppHceService.setUserId(this.user.getId());
                dataChanged = true;
            }
        } else
            synchronized (this.user) {
                if (!this.user.equals(user)&&user!=null) {
                    if(user.getId().compareTo(this.user.getId())!=0)
                        UserAppHceService.setUserId(user.getId());
                    this.user = user;
                    dataChanged = true;
                }
            }
        if (dataChanged)
            informSubscribers(this.user, null, null, null, null);
    }

    public void updateUserTicket(List<UserTicket> userTickets) {
        boolean dataChanged = false;
        synchronized (this.userTickets) {
            if (userTickets != null) {
                if (!(this.userTickets.containsAll(userTickets) && userTickets.containsAll(this.userTickets))) {
                    this.userTickets.clear();
                    this.userTickets.addAll(userTickets);
                    dataChanged = true;
                }

            }
        }
        if (dataChanged)
            informSubscribers(null, this.userTickets, null, null, null);
    }

    public void updateUserProfilePicture(byte[] pictureBytes) {
        if (pictureBytes != null) {
            this.userProfilePicture = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length, new BitmapFactory.Options());
            informSubscribers(null, null, null, null, this.userProfilePicture);
        }
    }
}
