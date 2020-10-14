package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PesaPalPayment extends AppCompatActivity {

    private String customerFirstName, customerLastName, customerEmailAddress, customerPhoneNumber, priceToPayNow, vehicle_title;
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

                            alertDialog.setMessage("Transaction id is: " + transactionId + "\n" + " url is: " + view.getUrl());
                            alertDialog.show();

                        }

                        super.onProgressChanged(view, newProgress);
                    }
                }


                );


            }
        });


    }
}