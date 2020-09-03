package com.example.kenyanrides;

import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kenyanrides.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    FloatingActionButton fabForgotPassword;

    TextView txtLogin, txtSignup;

    private EditText editTextEmailAddress, editTextMobileNumber, editTextPassword, editTextConfirmPassword;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private String FORGOT_PASSWORD_URL = "https://kenyanrides.com/android/forgot_password.php";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fabForgotPassword = findViewById(R.id.fab_forgot_password);

        txtLogin = findViewById(R.id.txt_login);

        txtSignup = findViewById(R.id.txt_signup);

        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);

        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);

        editTextPassword = findViewById(R.id.editTextPassword);

        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        alertDialogBuilder = new AlertDialog.Builder(this);

        progressDialog = new ProgressDialog(this);

        fabForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePassword();

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


    //called when button is clicked
    public void changePassword() {

        //check if network is connected
        if (!isNetworkAvailable()){

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.show();
            return;
        }

        String email = editTextEmailAddress.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(email)){
            editTextEmailAddress.setError("Email Address cannot be empty");
            return;
        }

        String mobileNumber = editTextMobileNumber.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(mobileNumber)){
            editTextMobileNumber.setError("Phone number cannot be empty");
            return;
        }


        String password = editTextPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Password cannot be empty");
            return;
        }

        String confirmPassword = editTextConfirmPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(confirmPassword)){
            editTextConfirmPassword.setError("Confirm Password cannot be empty");
            return;
        }

        //check if passwords match
        if (!password.equals(confirmPassword))
        {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Login and fetch result from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                FORGOT_PASSWORD_URL,
                response -> {

                    switch (response){

                        case "Password updated successfully":

                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);

                            progressDialog.dismiss();

                            break;

                        case "Error updating Password":

                            alertDialogBuilder.setMessage("Password update failed! Please try again\n ensure your internet connection is stable");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            alertDialogBuilder.show();

                            progressDialog.dismiss();

                        case "Credentials not valid":

                            alertDialogBuilder.setMessage("Error! Please ensure your email address and phone number is the one you used while registering");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            alertDialogBuilder.show();
                            progressDialog.dismiss();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String hashed = MD5_Hash(password);
                params.put("email_address", email);
                params.put("mobile_number", mobileNumber);
                params.put("new_password", hashed);

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