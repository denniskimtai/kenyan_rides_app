package com.example.kenyanrides;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String URL_GET_TOKEN = "https://kenyanrides.com/android/braintree/main.php";
    final String URL_CHECKOUT = "https://kenyanrides.com/android/braintree/checkout.php";

    private Button payWithCardButton;

    private ProgressDialog progressDialog;

    private String token;

    private Map<String, String> paramsHash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //intialization
        payWithCardButton = findViewById(R.id.payWithCardButton);

        payWithCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });

        progressDialog = new ProgressDialog(this);

        getToken();


    }

    private void submitPayment() {

        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {

            if (resultCode == RESULT_OK) {

                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                paramsHash = new HashMap<>();
                paramsHash.put("amount", String.valueOf(10));
                paramsHash.put("nonce", strNonce);

                sendPayments();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();

            }
        }

    }


    //make payment of car
    private void sendPayments() {

        //volley
        RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //check if transaction was successful
                        if (response.contains("Successful")){

                            //transaction is successful
                            Toast.makeText(PaymentActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();

                        }else{

                            //Transaction failed
                            Toast.makeText(PaymentActivity.this, "Transaction Failed \n Error: " + response, Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PaymentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if(paramsHash == null)
                    return null;


                Map<String, String> params = new HashMap<>();

                for (String key:paramsHash.keySet()){

                    params.put(key, paramsHash.get(key));

                }

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Content-type", "application/x-www-form-urlencoded");

                return params;

            }
        };

        queue.add(stringRequest);



    }

    //get token string from api
    private void getToken(){

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //Login and fetch result from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_GET_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        token = response;

                    }
                } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(PaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }

}