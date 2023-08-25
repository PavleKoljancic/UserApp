package com.example.userapp.activity.main.fragments.profile;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.token.TokenManager;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class UserProfileApiDecorator extends UserApiDecorator {

    List<UserTicket> getListUserTickets(User user) throws IOException {
        List<UserTicket> result = api.getUserTickets(user,TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
        return  result;
    }
    byte [] getUserProfilePicture(User user) throws IOException {
        Response<ResponseBody> result = api.getUserProfilePicture(user.getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        return result.body().bytes();

    }
}
