package com.example.userapp.activity.login;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import com.example.userapp.activity.ActivityController;
import com.example.userapp.activity.main.MainActivity;
import com.example.userapp.activity.nopicture.NoPictureActivity;
import com.example.userapp.activity.register.RegisterActivity;
import com.example.userapp.userdata.UserDataModel;

import org.json.JSONException;

import java.io.IOException;

class LoginController  extends ActivityController {
    private LoginActivity loginActivity;
    private  LoginModel loginModel;

    public LoginController(LoginActivity loginActivity)
    {
        super("Login Model Handler Thread");
        this.loginActivity = loginActivity;
        this.loginModel = new LoginModel();

    }

    public void onLoginClicked()
    {
        String emial = loginActivity.emailEditText.getText().toString();
        String password = loginActivity.passwordEditText.getText().toString();
        if(password==null||password.isEmpty()||emial==null||emial.isEmpty())
        {
            loginActivity.informText.setText("Polja ne smiju biti prazna!");
            loginActivity.informText.setVisibility(View.VISIBLE);
            return;
        }
        Handler handler = new Handler(handlerThread.getLooper());
        loginActivity.progressIndicator.setVisibility(View.VISIBLE);
        loginActivity.loginBtn.setEnabled(false);
        loginActivity.registerBtn.setEnabled(false);
        handler.post(()->attemptLogin(emial,password));


    }

    private void attemptLogin(String emial, String password){
        boolean loginResult=false;
        boolean loginFailure = false;
        try {
            loginResult =this.loginModel.attemptLogin(emial,password);
            if(loginResult)
                 this.userDataModel.updateUser(loginModel.loadUser());
        } catch (IOException|JSONException e ) {
            loginFailure =true;
        }
        finally {
            final boolean loginFailureRes =loginFailure;

            loginActivity.runOnUiThread(()-> updateUiAfterLoginAttempt());
            if(loginResult&&(!loginFailureRes)) {
                if (userDataModel.getUser() != null && userDataModel.getUser().getPictureHash() != null)
                    loginActivity.runOnUiThread(() -> startMainActivity());
                else loginActivity.runOnUiThread(() -> startNoPictureActivity());
            }
            else
                loginActivity.runOnUiThread(()-> setFailedLoginUpdateUI(loginFailureRes));
        }
    }

    private void setFailedLoginUpdateUI(boolean loginFailure) {
        if(loginFailure)
            this.loginActivity.informText.setText("Greška pri povezivanju");
        else this.loginActivity.informText.setText("Ne uspješna prijava");
    }

    private void updateUiAfterLoginAttempt()
    {
        loginActivity.progressIndicator.setProgress(100,true);
        loginActivity.loginBtn.setEnabled(true);
        loginActivity.registerBtn.setEnabled(true);
        loginActivity.progressIndicator.setVisibility(View.INVISIBLE);
        loginActivity.progressIndicator.setProgress(50,false);
    }

    private void startMainActivity()
    {
        this.loginActivity.startActivity(new Intent(this.loginActivity, MainActivity.class));
        this.loginActivity.finish();
    }
    private void startNoPictureActivity()
    {
        this.loginActivity.startActivity(new Intent(this.loginActivity, NoPictureActivity.class));
        this.loginActivity.finish();
    }
    public void register() {
        this.loginActivity.startActivity(new Intent(loginActivity, RegisterActivity.class));
    }


}
