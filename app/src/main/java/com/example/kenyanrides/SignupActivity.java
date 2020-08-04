package com.example.kenyanrides;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;

    TextView txtLogin;

    EditText EditTextFirstName, EditTextSecondName, EditTextId, EditTextPhoneNumber, EditTextEmail, EditTextPassword, EditTextConfirmPassword;

    String strFirstName,strSecondName,strNationalId,strPhoneNumber,strEmail,strPassword,strConfirmPassword, strRegDate;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        //initialize views
        alertDialogBuilder = new AlertDialog.Builder(this);

        EditTextFirstName = findViewById(R.id.edit_text_first_name);
        EditTextSecondName = findViewById(R.id.edit_text_second_name);
        EditTextId = findViewById(R.id.edit_text_id);
        EditTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        EditTextEmail = findViewById(R.id.edit_text_email);
        EditTextPassword = findViewById(R.id.edit_text_password);
        EditTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);


        txtLogin = findViewById(R.id.txt_login);



        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void Onregister(View view){

        if (!isNetworkAvailable()){

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.show();
            return;
        }

        //get text entered by user
        strFirstName = EditTextFirstName.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strFirstName)){
            EditTextFirstName.setError("Please enter your first name");
            return;
        }

        //get text entered by user
        strSecondName = EditTextSecondName.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strSecondName)){
            EditTextSecondName.setError("Please enter your second name");
            return;
        }

        //get text entered by user
        strNationalId = EditTextId.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strNationalId)){
            EditTextId.setError("Please enter your National Id");
            return;
        }

        //get text entered by user
        strPhoneNumber = EditTextPhoneNumber.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strPhoneNumber)){
            EditTextPhoneNumber.setError("Please enter your Phone Number");
            return;
        }

        //get text entered by user
        strEmail = EditTextEmail.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strEmail)){
            EditTextEmail.setError("Please enter your Email Address");
            return;
        }

        //get text entered by user
        strPassword = EditTextPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strPassword)){
            EditTextPassword.setError("Please enter a Password");
            return;
        }

        strConfirmPassword = EditTextConfirmPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(strConfirmPassword)){
            EditTextConfirmPassword.setError("Please confirm your password");

            return;
        }

        //check if passwords match
        if (!strPassword.equals(strConfirmPassword))
        {
            EditTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        strRegDate = sdf.format(Calendar.getInstance().getTime());


        String type = "register";

        BackgroundHelperClass backgroundHelperClass = new BackgroundHelperClass(this);

        backgroundHelperClass.execute(type,strFirstName, strSecondName, strNationalId, strPhoneNumber, strEmail, strPassword, strRegDate);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}