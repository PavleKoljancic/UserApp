package com.example.userapp.datamodel.user;

import android.graphics.Bitmap;

import com.example.userapp.models.Document;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;

import java.util.HashSet;
import java.util.List;

public interface UserDataChangeSubscriber {

    void onUserDataChanged(
            User user,
            HashSet<UserTicket> userTickets
            , Bitmap userProfilePicture, List<Document> userDocument
    );
}
