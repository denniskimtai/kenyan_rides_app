package com.example.kenyanrides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kenyanrides.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {

    TextView TxtSignup;

    TextView txtForgotPassword;

    FloatingActionButton fabLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TxtSignup = findViewById(R.id.txt_signup);

        txtForgotPassword = findViewById(R.id.txt_forgot_password);

        fabLogin = findViewById(R.id.fab_login);

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

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

}