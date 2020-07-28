package com.example.kenyanrides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kenyanrides.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignupActivity extends AppCompatActivity {

    FloatingActionButton fabSignup;

    TextView txtLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fabSignup = findViewById(R.id.fab_signup);

        txtLogin = findViewById(R.id.txt_login);

        fabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

}