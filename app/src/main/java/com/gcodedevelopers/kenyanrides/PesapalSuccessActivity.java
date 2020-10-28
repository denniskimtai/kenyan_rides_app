package com.gcodedevelopers.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PesapalSuccessActivity extends AppCompatActivity {

    private String vehicle_id, user_email, price_per_day, vehicle_owner_email, vehicle_title, pickupDate, pickupTime, vehicleTravelDestination,
            returnDate, returnTime, customerContactPhoneNumber, pickupLocation, returnLocation, owner_phone_number, priceToPayNow, transactionId, priceToPayLater;

    private ProgressDialog progressDialog;

    private String confirm_pesapal_payment = "https://kenyanrides.com/android/confirm_pesapal_payment.php";

    private LinearLayout layoutSuccess, layoutFailed;

    private TextView textViewFailed, textViewOwnerPhoneNumber;

    private Button btnTryAgain, btnCall, btnSms, btnProceed;

    private AlertDialog.Builder alertDialog;

    int k = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesapal_success);

        //get vehicle detail from intent
        vehicle_id = getIntent().getStringExtra("vehicle_id");
        user_email = getIntent().getStringExtra("user_email");
        price_per_day = getIntent().getStringExtra("price_per_day");
        owner_phone_number = getIntent().getStringExtra("owner_phone_number");
        vehicle_owner_email = getIntent().getStringExtra("vehicle_owner_email");
        pickupDate = getIntent().getStringExtra("pickup_date");
        pickupTime = getIntent().getStringExtra("pickup_time");
        vehicleTravelDestination = getIntent().getStringExtra("vehicle_travel_destination");
        returnDate = getIntent().getStringExtra("return_date");
        returnTime = getIntent().getStringExtra("return_time");
        customerContactPhoneNumber = getIntent().getStringExtra("customer_phone_number");
        pickupLocation = getIntent().getStringExtra("pickup_location");
        returnLocation = getIntent().getStringExtra("return_location");
        priceToPayNow = getIntent().getStringExtra("amount");
        priceToPayLater = getIntent().getStringExtra("price_to_pay_later");
        vehicle_title = getIntent().getStringExtra("vehicle_title");
        transactionId = getIntent().getStringExtra("transaction_id");

        //initialization
        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this);

        layoutFailed = findViewById(R.id.layout_failed);
        layoutSuccess = findViewById(R.id.layout_success);
        textViewFailed = findViewById(R.id.text_view_failed);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        textViewOwnerPhoneNumber = findViewById(R.id.text_view_phone_number);
        btnCall = findViewById(R.id.btn_call);
        btnSms = findViewById(R.id.btnSms);
        btnProceed = findViewById(R.id.btnProceed);

        //check if user has internet connection
        if (!isNetworkAvailable()){
            //if the user is not connected to the internet
            alertDialog.setTitle("Network Failure");
            alertDialog.setMessage("Please check your internet connection!");
            alertDialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    verifyPayment();

                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
            return;

        }else {
            verifyPayment();

        }



    }

    private void verifyPayment() {

        //show progress dialog
        progressDialog.setMessage("Verfiying payment...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //verify with volley from api
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                confirm_pesapal_payment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        switch (response){
                            case "payment verified":
                                k = 1;
                                layoutSuccess.setVisibility(View.VISIBLE);
                                layoutFailed.setVisibility(View.GONE);
                                textViewOwnerPhoneNumber.setText(owner_phone_number);

                                //onclick listener phone call or text
                                btnSms.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //open sms

                                        Uri uri = Uri.parse("smsto:" + owner_phone_number);
                                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                        startActivity(intent);


                                    }
                                });


                                btnCall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //check if permission is granted
                                        int permissionCheck = ContextCompat.checkSelfPermission(PesapalSuccessActivity.this, Manifest.permission.CALL_PHONE);

                                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(
                                                    PesapalSuccessActivity.this,
                                                    new String[]{Manifest.permission.CALL_PHONE},
                                                    101);
                                        } else {

                                            //open dial pad
                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + owner_phone_number));
                                            startActivity(intent);

                                        }


                                    }
                                });


                                //set on back pressed to go to home activity

                                break;

                            case "payment failed":

                                k = 2;

                                layoutSuccess.setVisibility(View.GONE);
                                layoutFailed.setVisibility(View.VISIBLE);

                                textViewFailed.setText("Payment of " + priceToPayNow + " for " + vehicle_title +" failed");

                                btnTryAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        //go back to payment activity
                                        finish();

                                    }
                                });

                                break;

                            case "payment pending":

                                k = 3;

                                layoutSuccess.setVisibility(View.GONE);
                                layoutFailed.setVisibility(View.VISIBLE);
                                btnProceed.setText("Check payment Status");
                                textViewFailed.setText("Payment of " + priceToPayNow + " for " + vehicle_title +" is currently PENDING");
                                btnProceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        verifyPayment();
                                    }
                                });

                                break;

                            default:
                                alertDialog.setMessage("Ensure your network is okay to be able to complete booking!");
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        verifyPayment();
                                    }
                                });
                                alertDialog.show();
                                break;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PesapalSuccessActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("pickupDate", pickupDate);
                params.put("pickupTime", pickupTime);
                params.put("vehicleTravelDestination", vehicleTravelDestination);
                params.put("returnDate", returnDate);
                params.put("returnTime", returnTime);
                params.put("phoneNumber", customerContactPhoneNumber);
                params.put("pickupLocation", pickupLocation);
                params.put("returnLocation", returnLocation);
                params.put("vehicle_id", vehicle_id);
                params.put("userEmail", user_email);
                params.put("price_per_day", String.valueOf(priceToPayLater));
                params.put("vehicle_owner_email", vehicle_owner_email);
                params.put("vehicle_title", vehicle_title);
                params.put("transaction_id", transactionId);
                params.put("service_fee", String.valueOf(priceToPayNow));

                return params;

            }
        };

        Volley.newRequestQueue(PesapalSuccessActivity.this).add(stringRequest);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {

        if (k == 1){

            //go to home activity
            Intent i = new Intent(PesapalSuccessActivity.this, MainActivity.class);
            startActivity(i);

        } else if (k == 2){
            //go to payment activity
            finish();

        }else if (k ==3){

            //show alert dialog
            alertDialog.setTitle("Are you sure you want to exit this page? ");
            alertDialog.setMessage("Leaving this page will not complete your booking. \nUse the try again button to keep checking status of your payment. ");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Exit Page", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();

                }
            });

            alertDialog.setNegativeButton("Stay on page", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();

        }

    }
}