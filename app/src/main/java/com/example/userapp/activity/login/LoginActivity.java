package com.example.userapp.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.userapp.R;
import com.example.userapp.datamodel.CacheLayer;
import com.example.userapp.token.TokenManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    CircularProgressIndicator progressIndicator;
    TextView informText;
    EditText passwordEditText;
    EditText emailEditText;

    Button loginBtn;
    Button registerBtn;
    LoginController loginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheLayer.createCacheLayer(getCacheDir());
        setContentView(R.layout.activity_login);
        try {
            TokenManager.setToken(null);
        } catch (JSONException e) {

        }

        progressIndicator =findViewById(R.id.progressLogin);
         informText= findViewById(R.id.loginInformText);
         passwordEditText = findViewById(R.id.passwordField);
         emailEditText = findViewById(R.id.emialField);

         loginBtn =findViewById(R.id.loginBtn);
         registerBtn = findViewById(R.id.Potvrdi);

        loginController = new LoginController(this);
        Thread bgThread = new Thread(()-> {

            loginController.loadCacheIfPresent();

        });
        bgThread.start();
         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 loginController.onLoginClicked();
             }
         });

         registerBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 loginController.register();
             }
         });
    }

    @Override
    protected void onDestroy() {
        loginController.quitHandlerThread();
        super.onDestroy();
    }
}