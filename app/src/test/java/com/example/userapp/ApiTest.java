package com.example.userapp;



import com.example.userapp.models.TicketType;
import com.example.userapp.models.User;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.retrofit.RetrofitService;
import com.example.userapp.retrofit.UserAPI;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ApiTest {

    @Test
    public void userInitTest() throws IOException, JSONException {
        UserAPI api = RetrofitService.getApi();
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail("pavle.koljancic@gmail.com");
        loginData.setPasswordHash("password123");
        loginData.setFirstName("Pavle");
        loginData.setLastName("Koljancic");
        Integer registerResult = api.registerUser(loginData).execute().body();
        String Token = api.loginUser(loginData).execute().body();
        TokenManager.setToken(Token);
        User user;
        if("USER".equals(TokenManager.getInstance().getRole()))
         user = api.getUser(TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();

        System.out.println();
    }



  }
