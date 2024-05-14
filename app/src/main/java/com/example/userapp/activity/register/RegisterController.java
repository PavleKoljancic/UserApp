package com.example.userapp.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.activity.login.LoginActivity;

import com.example.userapp.activity.main.MainActivity;
import com.example.userapp.activity.nopicture.NoPictureActivity;
import com.example.userapp.models.Document;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.models.UserWithPassword;
import com.example.userapp.datamodel.user.UserDataChangeSubscriber;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController extends AbstractViewController implements UserDataChangeSubscriber {
    private RegisterActivity registerActivity;
    private RegisterApiDecorator registerApiDecorator;


    RegisterController(RegisterActivity registerActivity) {
        super("HANDLER THREAD NO PICTURE ACTIVITY");
        this.registerActivity = registerActivity;
        this.registerApiDecorator = new RegisterApiDecorator();
        this.userDataModel.subscribeToDataChange(this);

    }

    void onRegister()
    {
        if(!checkFields()) {

            registerActivity.informText.setText("Polja ne smiju biti prazna");
            return;
        }
        if(!(registerActivity.password.getText().toString().equals(registerActivity.passwordConfirm.getText().toString())))
        {
            registerActivity.informText.setText("Lozinke se ne poklapaju");
            return ;


        }
        Pattern mailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mailMatcher = mailPattern.matcher(registerActivity.email.getText());

        if(!mailMatcher.matches())
        {
            registerActivity.informText.setText("Unesite validnu e-mail adresu");
            return;
        }

        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");
        Matcher passwordMatcher = passwordPattern.matcher(registerActivity.password.getText());
        if(!passwordMatcher.matches())
        {
            registerActivity.informText.setText("Lozinka mora da bude minimalno 6 karaktera, da sadrži mala i velika slova, broj i  specijalni znak");
            return;
        }

        registerActivity.progressIndicator.setVisibility(View.VISIBLE);
        register(userWithPasswordFromFields());
    }
    void register(UserWithPassword userWithPassword) {   //Start UI



        Handler handler = new Handler(handlerThread.getLooper());

        handler.post(() -> {
            boolean isRegistard = false;
            boolean isLoggedIn = false;
            boolean desilaSeGreska = false;
            try {
                isRegistard = this.registerApiDecorator.attemptRegister(userWithPassword);
                if(isRegistard)
                    isLoggedIn = this.registerApiDecorator.attemptLogin(userWithPassword);
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
                        this.userDataModel.updateUser(this.registerApiDecorator.loadUser(),this.registerApiDecorator.loadUserKey());
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
        if(registerActivity.passwordConfirm.getText().toString()==null||registerActivity.passwordConfirm.getText().toString().isEmpty())
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
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets,
                                  Bitmap userProfilePicture,
                                  List<Document> userDocuments) {
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
