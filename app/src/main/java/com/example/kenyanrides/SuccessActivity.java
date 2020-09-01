package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    private TextView textViewPhoneNumber;
    private Button btnCall, btnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        btnCall = findViewById(R.id.btn_call);
        btnSms = findViewById(R.id.btnSms);
        textViewPhoneNumber = findViewById(R.id.text_view_phone_number);

        String phoneNumber = getIntent().getStringExtra("owner_phone_number");

        textViewPhoneNumber.setText("Owner contact number: " + phoneNumber);

        //onclick listener phone call or text
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open sms
                String number = phoneNumber;  // The number on which you want to send SMS

                Uri uri = Uri.parse("smsto:" + number);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);


            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if permission is granted
                int permissionCheck = ContextCompat.checkSelfPermission(SuccessActivity.this, Manifest.permission.CALL_PHONE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            SuccessActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            101);
                } else {

                    //open dial pad
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);

                }


            }
        });


    }
}