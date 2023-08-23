package com.example.userapp.activity;

import com.example.userapp.models.User;
import com.example.userapp.retrofit.RetrofitService;
import com.example.userapp.retrofit.UserAPI;
import com.example.userapp.singeltons.TokenManager;

import org.json.JSONException;

import java.io.IOException;

public abstract class ActivityModel {

    protected UserAPI api;
    protected ActivityModel()
    {
        this.api= RetrofitService.getApi();
    }

    public User loadUser() throws IOException, JSONException {

        User temp = api.getUser(TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
        return  temp;
    }
}
