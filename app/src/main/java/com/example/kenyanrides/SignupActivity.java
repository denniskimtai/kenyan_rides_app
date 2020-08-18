package com.example.kenyanrides;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;
    private String register_url = "https://kenyanrides.com/android/registration.php";

    TextView txtLogin;

    EditText EditTextFirstName, EditTextSecondName, EditTextId, EditTextPhoneNumber, EditTextEmail, EditTextPassword, EditTextConfirmPassword;

    String strFirstName,strSecondName,strNationalId,strPhoneNumber,strEmail,strPassword,strConfirmPassword, strRegDate;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        //initialize views
        alertDialogBuilder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);

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


        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Login and fetch result from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, register_url,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

                    //fetch json object returned by api
                    switch (response) {

                        case "User already registered":
                            alertDialogBuilder.setTitle("Registration Failed");
                            alertDialogBuilder.setMessage("The email already exists in the system! Please login ");
                            alertDialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {

                                Intent regintent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(regintent);
                                finish();

                            });
                            alertDialogBuilder.show();

                            break;


                        case "registration successful":
                            Intent regintent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(regintent);
                            finish();

                            break;

                        case "registration failed":
                            alertDialogBuilder.setTitle("Registration Failed");
                            alertDialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {

                            });
                            alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
                            alertDialogBuilder.show();

                            break;

                    }
                    progressDialog.dismiss();

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                String type = "register";
//
//                BackgroundHelperClass backgroundHelperClass = new BackgroundHelperClass(this);
//
//                backgroundHelperClass.execute(type,strFirstName, strSecondName, strNationalId, strPhoneNumber, strEmail, strPassword, strRegDate);
                String hashed = MD5_Hash(strPassword);
                params.put("first_name", strFirstName);
                params.put("second_name", strSecondName);
                params.put("national_id", strNationalId);
                params.put("mobile_number", strPhoneNumber);
                params.put("email_address", strEmail);
                params.put("password", hashed);
                params.put("reg_date", strRegDate);

                return params;

            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String MD5_Hash (String s){
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

}