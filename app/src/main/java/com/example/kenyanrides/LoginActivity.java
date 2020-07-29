package com.example.kenyanrides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {

    TextView TxtSignup;

    TextView txtForgotPassword;

    FloatingActionButton fabLogin;

    EditText EditTextEmail, EditTextPassword;

    private Context mcontext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mcontext = this;

        //Initialize views

        TxtSignup = findViewById(R.id.txt_signup);

        txtForgotPassword = findViewById(R.id.txt_forgot_password);

        fabLogin = findViewById(R.id.fab_login);

        EditTextEmail = findViewById(R.id.edit_text_email);

        EditTextPassword = findViewById(R.id.edit_text_password);

        TxtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();

            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    //called when button is clicked
    public void login(View view) {
        String email = EditTextEmail.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(email)){
            EditTextEmail.setError("Email Address cannot be empty");
            return;
        }


        String password = EditTextPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(password)){
            EditTextPassword.setError("Password cannot be empty");
            return;
        }

        String type = "login";

        BackgroungHelperClass backgroungHelperClass = new BackgroungHelperClass(mcontext);

        backgroungHelperClass.execute(type,email, password);

    }

}