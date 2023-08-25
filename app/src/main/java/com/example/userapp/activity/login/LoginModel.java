package com.example.userapp.activity.login;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;

class LoginModel extends UserApiDecorator {
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
