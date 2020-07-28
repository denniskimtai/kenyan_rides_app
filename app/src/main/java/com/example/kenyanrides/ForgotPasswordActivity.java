package com.example.kenyanrides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kenyanrides.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    FloatingActionButton fabForgotPassword;

    TextView txtLogin, txtSignup;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fabForgotPassword = findViewById(R.id.fab_forgot_password);

        txtLogin = findViewById(R.id.txt_login);

        txtSignup = findViewById(R.id.txt_signup);

        fabForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ForgotPasswordActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

}