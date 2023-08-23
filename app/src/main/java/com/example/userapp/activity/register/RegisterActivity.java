package com.example.userapp.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.userapp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class RegisterActivity extends AppCompatActivity {

    RegisterController registerController;
    Button registerBtn;
    EditText password;
    EditText email;
    EditText name;
    EditText lastname;

    CircularProgressIndicator progressIndicator;
    TextView informText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


         registerBtn=findViewById(R.id.attemptRegisterBtn);
         password= findViewById(R.id.passwordFieldReg);
         email= findViewById(R.id.emialFieldReg);
         name=findViewById(R.id.nameEditTextTextReg);
         lastname=findViewById(R.id.surnameEeditTextReg);
         progressIndicator=findViewById(R.id.progressRegister);
         informText=findViewById(R.id.reginformText);
        this.registerController = new RegisterController(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerController.onRegister();
            }
        });

    }

    @Override
    protected void onDestroy() {
        registerController.quitHandlerThread();
        super.onDestroy();
    }
}