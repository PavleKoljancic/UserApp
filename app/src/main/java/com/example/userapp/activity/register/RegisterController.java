package com.example.userapp.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.userapp.activity.ActivityController;
import com.example.userapp.activity.login.LoginActivity;

import com.example.userapp.activity.main.MainActivity;
import com.example.userapp.activity.nopicture.NoPictureActivity;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.userdata.UserDataChangeSubscriber;
import com.example.userapp.userdata.UserDataModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;

public class RegisterController extends ActivityController implements UserDataChangeSubscriber {
    private RegisterActivity registerActivity;
    private RegisterModel registerModel;


    RegisterController(RegisterActivity registerActivity) {
        super("HANDLER THREAD NO PICTURE ACTIVITY");
        this.registerActivity = registerActivity;
        this.registerModel = new RegisterModel();
        this.userDataModel.subscribeToDataChange(this);

    }

    void onRegister() {   //Start UI
        UserWithPassword userWithPassword = new UserWithPassword();


        Handler handler = new Handler(handlerThread.getLooper());

        handler.post(() -> {
            boolean isRegistard = false;
            boolean isLoggedIn = false;
            boolean desilaSeGreska = false;
            try {
                isRegistard = this.registerModel.attemptRegister(userWithPassword);
                isLoggedIn = this.registerModel.attemptLogin(userWithPassword);
            } catch (IOException | JSONException e) {
                desilaSeGreska = true;
            } finally {
                if (isRegistard) {
                    //Uspjesno registrovan
                }
                else if(!desilaSeGreska){  //neuspjesno registrovan
                     }
                else { //error
                     }
            }


                if (isLoggedIn) {
                    try {
                        this.userDataModel.updateUser(this.registerModel.loadUser());
                    } catch (IOException | JSONException e) {
                        //Ovdje na Login i unsubscribuj
                        this.userDataModel.unsubscribeToDataChange(this);
                        this.registerActivity.runOnUiThread(()-> {
                            this.registerActivity.startActivity(new Intent(this.registerActivity, NoPictureActivity.class));
                            this.registerActivity.finish();
                        });
                    }
                }

        });
    }

    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, HashSet<TicketRequestResponse> ticketRequestResponses, HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture) {
        if (user != null) {
            this.userDataModel.unsubscribeToDataChange(this);
            this.registerActivity.runOnUiThread(() -> {

                if (user.getPictureHash() != null)
                    this.registerActivity.startActivity(new Intent(this.registerActivity, MainActivity.class));
                else this.registerActivity.startActivity(new Intent(this.registerActivity, NoPictureActivity.class));
                this.registerActivity.finish();

            });
        }
    }

}
