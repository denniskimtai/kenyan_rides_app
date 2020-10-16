package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PesaPalPayment extends AppCompatActivity {

    private String customerFirstName, customerLastName, customerEmailAddress, priceToPayNow;

    private String vehicle_id, user_email, price_per_day, vehicle_owner_email, vehicle_title, pickupDate, pickupTime, vehicleTravelDestination,
            returnDate, returnTime, customerPhoneNumber, customerContactPhoneNumber, pickupLocation, returnLocation, mpesaNumber, owner_phone_number;

    private ProgressDialog progressDialog;

    private String PESAPAL_PAYMENT_PAGE_URL = "https://kenyanrides.com/android/pesapal_iframe.php";

    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesa_pal_payment);

        //initialization
        WebView browser = findViewById(R.id.webview);
        alertDialog = new AlertDialog.Builder(this);

        //get customer information from intent
        customerFirstName = getIntent().getStringExtra("first_name");
        customerLastName = getIntent().getStringExtra("last_name");
        customerEmailAddress = getIntent().getStringExtra("email_address");
        customerPhoneNumber = getIntent().getStringExtra("phone_number");
        priceToPayNow = getIntent().getStringExtra("amount");
        vehicle_title = getIntent().getStringExtra("vehicle_title");

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


        //initialization
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading payment page. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        //pesapal implementation
        try {
            loadPaymentPage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void loadPaymentPage() throws UnsupportedEncodingException {

        WebView webview = new WebView(this);

        setContentView(webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(false);

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);

        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);

        String postData = "first_name=" + URLEncoder.encode(customerFirstName, "UTF-8")
                + "&last_name=" + URLEncoder.encode(customerLastName, "UTF-8")
                + "&email_address=" + URLEncoder.encode(customerEmailAddress, "UTF-8")
                + "&phone_number=" + URLEncoder.encode(customerPhoneNumber, "UTF-8")
                + "&amount=" + URLEncoder.encode("1", "UTF-8");

        webview.postUrl(PESAPAL_PAYMENT_PAGE_URL,postData.getBytes());

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();

                webview.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {

                        //if callback is called
                        if (view.getUrl().contains("pesapal_callback.php")) {

                            String transactionId = view.getUrl();
                            transactionId = transactionId.substring(transactionId.indexOf("=") + 1);
                            transactionId = transactionId.substring(0, transactionId.indexOf("&"));

                            Intent intent = new Intent(PesaPalPayment.this, PesapalSuccessActivity.class);

                            intent.putExtra("transaction_id", transactionId);
                            //car booked details
                            intent.putExtra("vehicle_id", vehicle_id);
                            intent.putExtra("user_email", user_email);
                            intent.putExtra("price_per_day", price_per_day);
                            intent.putExtra("owner_phone_number", owner_phone_number);
                            intent.putExtra("vehicle_owner_email", vehicle_owner_email);
                            intent.putExtra("pickup_date", pickupDate);
                            intent.putExtra("pickup_time", pickupTime);
                            intent.putExtra("vehicle_travel_destination", vehicleTravelDestination);
                            intent.putExtra("return_date", returnDate);
                            intent.putExtra("return_time", returnTime);
                            intent.putExtra("customer_phone_number", customerPhoneNumber);
                            intent.putExtra("pickup_location", pickupLocation);
                            intent.putExtra("return_location", returnLocation);
                            intent.putExtra("amount", String.valueOf(priceToPayNow));
                            intent.putExtra("vehicle_title", vehicle_title);


                            finish();
                            startActivity(intent);

                        }

                        super.onProgressChanged(view, newProgress);
                    }
                }


                );


            }
        });


    }
}