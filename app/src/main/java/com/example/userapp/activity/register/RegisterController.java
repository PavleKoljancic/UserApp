package com.example.userapp.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

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

    void onRegister()
    {
        if(checkFields()) {
            registerActivity.progressIndicator.setVisibility(View.VISIBLE);
            register(userWithPasswordFromFields());}
        else registerActivity.informText.setText("Polja ne smiju biti prazna");
    }
    void register(UserWithPassword userWithPassword) {   //Start UI



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
                    registerActivity.runOnUiThread(()->registerActivity.informText.setText("Uspješno registrovan!"));
                }
                else if(!desilaSeGreska){
                    registerActivity.runOnUiThread(()->registerActivity.informText.setText("Email adresa je zauzeta"));
                     }
                else { registerActivity.runOnUiThread(()->registerActivity.informText.setText("Desila se greška pri povezivanju"));
                     }
            }


                if (isLoggedIn) {
                    try {
                        this.userDataModel.updateUser(this.registerModel.loadUser());
                        registerActivity.runOnUiThread(()-> registerActivity.progressIndicator.setVisibility(View.INVISIBLE));
                    } catch (IOException | JSONException e) {

                        this.userDataModel.unsubscribeToDataChange(this);
                        this.registerActivity.runOnUiThread(()-> {
                            registerActivity.progressIndicator.setVisibility(View.INVISIBLE);
                            this.registerActivity.startActivity(new Intent(this.registerActivity, LoginActivity.class));
                            this.registerActivity.finish();
                        });
                    }
                }
                else registerActivity.runOnUiThread(()-> registerActivity.progressIndicator.setVisibility(View.INVISIBLE));

        });
    }

    private boolean checkFields()
    {
        if(registerActivity.name.getText().toString()==null||registerActivity.name.getText().toString().isEmpty())
            return false;
        if(registerActivity.lastname.getText().toString()==null||registerActivity.lastname.getText().toString().isEmpty())
            return false;
        if(registerActivity.password.getText().toString()==null||registerActivity.password.getText().toString().isEmpty())
            return false;
        if(registerActivity.email.getText().toString()==null||registerActivity.email.getText().toString().isEmpty())
            return false;
        return true;
    }

    private UserWithPassword userWithPasswordFromFields()
    {
        UserWithPassword userWithPassword = new UserWithPassword();
        userWithPassword.setLastName(registerActivity.lastname.getText().toString());
        userWithPassword.setFirstName(registerActivity.name.getText().toString());
        userWithPassword.setPasswordHash(registerActivity.password.getText().toString());
        userWithPassword.setEmail(registerActivity.email.getText().toString());

        return userWithPassword;
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
