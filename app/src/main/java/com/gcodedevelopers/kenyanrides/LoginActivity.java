package com.gcodedevelopers.kenyanrides;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;

    TextView TxtSignup;

    TextView txtForgotPassword;

    FloatingActionButton fabLogin;

    EditText EditTextEmail, EditTextPassword;

    private Context mcontext;

    private ProgressDialog progressDialog;

    String login_url = "https://kenyanrides.com/android/login.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mcontext = this;

        //Initialize views

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In. Please wait...");

        alertDialogBuilder = new AlertDialog.Builder(this);

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

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }

    //called when button is clicked
    public void login() {

        //check if network is connected
        if (!isNetworkAvailable()){

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.show();
            return;
        }

        String email = EditTextEmail.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(email)){
            EditTextEmail.setError("Email Address cannot be empty");
            return;
        }


        String password = EditTextPassword.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(password)){
            EditTextPassword.setError("Password cannt be empty");
            return;
        }

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Login and fetch result from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                login_url,
                response -> {

                    //fetch json object returned by api
                    try {

                        //converting response to json object
                        JSONObject obj = new JSONObject(response);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            //Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");

                            //creating a new user object
                            user user = new user(
                                    userJson.getInt("id"),
                                    userJson.getInt("national_id"),
                                    userJson.getString("first_name"),
                                    userJson.getString("second_name"),
                                    userJson.getString("email"),
                                    userJson.getString("mobile_number"),
                                    userJson.getString("reg_date"),
                                    userJson.getString("dob"),
                                    userJson.getString("Address"),
                                    userJson.getString("city"),
                                    userJson.getString("country")

                            );

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(this).userLogin(user);

                            //start main activity
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            alertDialogBuilder.setTitle("Login Failed");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
                            alertDialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {

                            });
                            alertDialogBuilder.show();
                        }
                        progressDialog.dismiss();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String hashed = MD5_Hash(password);
                params.put("email", email);
                params.put("password", hashed);

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