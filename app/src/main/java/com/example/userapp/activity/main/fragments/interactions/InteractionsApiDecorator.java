package com.example.userapp.activity.main.fragments.interactions;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.Route;
import com.example.userapp.models.ScanInteraction;
import com.example.userapp.models.Transaction;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public class InteractionsApiDecorator extends UserApiDecorator {


    List<ScanInteraction> getUserScanInteractions() throws JSONException, IOException {

        return   api.getUserScanInteractions(TokenManager.getInstance().getId(),TokenManager.bearer()+TokenManager.getInstance().getToken()).execute().body();
    }


    List<Route>getRoutes() throws IOException {
        return api.getRoutes(TokenManager.bearer()+TokenManager.getInstance().getToken()).execute().body();
    }

    @GET("/api/transactions/getTransactionsForUser={UserId}")
    List<Transaction> getUserTransactions() throws JSONException, IOException {
       return   api.getUserTransactions(TokenManager.getInstance().getId(),TokenManager.bearer()+TokenManager.getInstance().getToken()).execute().body();
    }



}
