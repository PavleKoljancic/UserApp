package com.example.userapp;


import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.retrofit.RetrofitService;
import com.example.userapp.retrofit.UserAPI;
import com.example.userapp.singeltons.TokenManager;

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
import retrofit2.Call;
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
        User user = api.getUser(TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();

        System.out.println();
    }

    @Test
    public void userFiles() throws IOException, JSONException {
        UserAPI api = RetrofitService.getApi();
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail("pavle.koljancic@gmail.com");
        loginData.setPasswordHash("password123");
        loginData.setFirstName("Pavle");
        loginData.setLastName("Koljancic");
        String Token = api.loginUser(loginData).execute().body();
        TokenManager.setToken(Token);

        File file = new File(      "/home/pavle/Downloads/a.png"); // initialize file here


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("profile_pic", file.getName(), requestFile);


        Response<Boolean> resultImage = api.uploadProfilePicture(body, TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        System.out.println();
    }

    @Test
    public void userFiles2() throws IOException, JSONException {
        UserAPI api = RetrofitService.getApi();
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail("pavle.koljancic@gmail.com");
        loginData.setPasswordHash("password123");
        loginData.setFirstName("Pavle");
        loginData.setLastName("Koljancic");
        String Token = api.loginUser(loginData).execute().body();
        TokenManager.setToken(Token);

        File file = new File(      "/home/pavle/Desktop/FAKS/2/OEDT/Auditorne/bipolarni_tranzistori.pdf"); // initialize file here


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("pdf"),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("document", file.getName(), requestFile);


        Response<Boolean> result = api.uploadDocument(body, TokenManager.getInstance().getId(),"JEBI SE DEKI" ,TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        System.out.println();
    }


    @Test
    public void documentsGetAndDelete() throws IOException, JSONException {
        UserAPI api = RetrofitService.getApi();
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail("pavle.koljancic@gmail.com");
        loginData.setPasswordHash("password123");
        loginData.setFirstName("Pavle");
        loginData.setLastName("Koljancic");
        String Token = api.loginUser(loginData).execute().body();
        TokenManager.setToken(Token);


        Response<ResponseBody> response = api.getDocument(TokenManager.getInstance().getId(), "JEBI SE DEKI", TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        byte[] documentBytes = response.body().bytes();
         new FileOutputStream("/home/pavle/Downloads/deki.pdf").write(documentBytes);
        System.out.println();
        Response<Boolean> response2 = api.removeDocument(TokenManager.getInstance().getId(), "JEBI SE DEKI", TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        System.out.println();
    }

    @Test
    public void getTickets() throws IOException, JSONException {
        UserAPI api = RetrofitService.getApi();
        UserWithPassword loginData = new UserWithPassword();
        loginData.setEmail("pavle.koljancic@gmail.com");
        loginData.setPasswordHash("password123");
        loginData.setFirstName("Pavle");
        loginData.setLastName("Koljancic");
        String Token = api.loginUser(loginData).execute().body();
        TokenManager.setToken(Token);


        Response<List<TicketType>> result = api.getTicketsInUse(0, 50, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        List<TicketType> tickets = result.body();

        Response<String> response = api.addTicketRequest(1, TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
        User user = new User();
        user.setId(TokenManager.getInstance().getId());
        Response<List<TicketRequest>> ticketRequests = api.getUnprocessTicketRequests(user.getId(), 0, 50, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();

    }

  }
