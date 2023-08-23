package com.example.userapp.activity.login;

import com.example.userapp.activity.ActivityModel;
import com.example.userapp.models.User;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.retrofit.RetrofitService;
import com.example.userapp.retrofit.UserAPI;
import com.example.userapp.singeltons.TokenManager;

import org.json.JSONException;

import java.io.IOException;

class LoginModel extends ActivityModel {
    private  LoginController loginController;





    public boolean attemptLogin(String eMail, String password) throws IOException, JSONException {
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail(eMail);
        loginData.setPasswordHash(password);
        String Token = api.loginUser(loginData).execute().body();
        if(Token==null)
            return false;
        TokenManager.setToken(Token);
        if("USER".equals(TokenManager.getInstance().getRole()))
            return true;
        return false;
    }


}
