package com.example.userapp.activity.register;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;

public class RegisterApiDecorator extends UserApiDecorator {

     boolean attemptRegister(UserWithPassword userWithPassword) throws IOException, JSONException {

        Integer registerResult = api.registerUser(userWithPassword).execute().body();
        if(registerResult==null||registerResult<1)
            return false;

        return true;
    }

     boolean attemptLogin(UserWithPassword userWithPassword) throws JSONException, IOException {
        String Token = api.loginUser(userWithPassword).execute().body();
        if(Token==null)
            return false;
        TokenManager.setToken(Token);
        return "USER".equals(TokenManager.getInstance().getRole());
    }
}
