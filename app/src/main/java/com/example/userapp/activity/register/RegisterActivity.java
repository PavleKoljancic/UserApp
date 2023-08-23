package com.example.userapp.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.userapp.R;

public class RegisterActivity extends AppCompatActivity {

    RegisterController registerController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.registerController = new RegisterController(this);

    }

    @Override
    protected void onDestroy() {
        registerController.quitHandlerThread();
        super.onDestroy();
    }
}