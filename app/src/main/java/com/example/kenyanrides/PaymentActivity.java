package com.example.kenyanrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import Model.AccessToken;
import Model.STKPush;
import Services.DarajaApiClient;
import Services.Utils;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.kenyanrides.Constants.BUSINESS_SHORT_CODE;
import static com.example.kenyanrides.Constants.CALLBACKURL;
import static com.example.kenyanrides.Constants.PARTYB;
import static com.example.kenyanrides.Constants.PASSKEY;
import static com.example.kenyanrides.Constants.TRANSACTION_TYPE;


public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String URL_GET_TOKEN = "https://kenyanrides.com/android/braintree/main.php";
    final String URL_CHECKOUT = "https://kenyanrides.com/android/braintree/checkout.php";

    private Button payWithMpesaButton;

    private LinearLayout layoutMpesa, layoutCreditCard;

    private TextInputLayout mpesaNumberInput;

    private ProgressDialog progressDialog;

    private AlertDialog.Builder alertDialog;

    private String token;

    private Map<String, String> paramsHash;

    private String vehicle_id, user_email, price_per_day, vehicle_owner_email, vehicle_title, pickupDate, pickupTime, vehicleTravelDestination,
            returnDate, returnTime, customerPhoneNumber, pickupLocation, returnLocation, mpesaNumber, owner_phone_number;

    private int priceToPayNow, priceToPayLater;

    private EditText editTextMpesaNumber;

    private DarajaApiClient mApiClient;

    private TextView textViewTotalPrice, textViewServiceFee, textViewRemainingAmount;

    private AlertDialog.Builder alertDialogBuilder;

    private String confirm_payment_url = "https://kenyanrides.com/android/verify_payment.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        //get strings from booknow activity
        vehicle_id = getIntent().getStringExtra("vehicle_id");
        user_email = getIntent().getStringExtra("user_email");
        price_per_day = getIntent().getStringExtra("price_per_day");
        owner_phone_number = getIntent().getStringExtra("owner_phone_number");
        vehicle_owner_email = getIntent().getStringExtra("vehicle_owner_email");
        vehicle_title = getIntent().getStringExtra("vehicle_title");
        pickupDate = getIntent().getStringExtra("pickupDate");
        pickupTime = getIntent().getStringExtra("pickupTime");
        vehicleTravelDestination = getIntent().getStringExtra("vehicleTravelDestination");
        returnDate = getIntent().getStringExtra("returnDate");
        returnTime = getIntent().getStringExtra("returnTime");
        customerPhoneNumber = getIntent().getStringExtra("customerPhoneNumber");
        pickupLocation = getIntent().getStringExtra("pickupLocation");
        returnLocation = getIntent().getStringExtra("returnLocation");

        //intialization
        payWithMpesaButton = findViewById(R.id.payWithCardButton);

        alertDialogBuilder = new AlertDialog.Builder(this);

        layoutCreditCard = findViewById(R.id.layout_credit_card);

        layoutMpesa = findViewById(R.id.layout_mpesa);

        mpesaNumberInput = findViewById(R.id.mpesa_number);

        editTextMpesaNumber = findViewById(R.id.edit_Text_mpesa_number);

        textViewTotalPrice = findViewById(R.id.text_view_total_price);

        textViewServiceFee = findViewById(R.id.text_view_service_fee);

        textViewRemainingAmount = findViewById(R.id.text_view_remaining_amount);


        //convert price
        priceToPayLater = (int)Math.round(Integer.parseInt(price_per_day)/1.1 * 100) / 100;

        priceToPayNow = Integer.parseInt(price_per_day) - priceToPayLater;


        //set price
        textViewTotalPrice.setText("Ksh " + String.valueOf(price_per_day));
        textViewServiceFee.setText("Ksh " + String.valueOf(priceToPayNow));
        textViewRemainingAmount.setText("Ksh " + String.valueOf(priceToPayLater));

        //on click listeners

        layoutMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMpesa.setBackgroundColor(Color.parseColor("#A6D1F1B5"));
                layoutCreditCard.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });

        layoutCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutCreditCard.setBackgroundColor(Color.parseColor("#C2DEEF"));
                layoutMpesa.setBackgroundColor(getResources().getColor(android.R.color.white));
                submitPayment();

            }
        });

        //set text of button
        payWithMpesaButton.setText("Pay Ksh " + priceToPayNow + " with M-Pesa");

        payWithMpesaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pay with mpesa

                //check if number has been entered
                mpesaNumber = editTextMpesaNumber.getText().toString();
                if(TextUtils.isEmpty(mpesaNumber)){
                    editTextMpesaNumber.setError("Please enter your Mpesa number that you will pay with");
                    return;
                }

                //mpesa implementation
                performSTKPush(mpesaNumber,"1");


            }
        });

        progressDialog = new ProgressDialog(this);

        alertDialog = new AlertDialog.Builder(this);

        //mpesa token

        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

        //card payment token
        getToken();




    }

    //mpesa implementation

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull retrofit2.Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }


    public void performSTKPush(String phone_number,String amount) {
        progressDialog.setMessage("Processing your request");
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "MPESA Android Test", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull retrofit2.Response<STKPush> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        //show alert dialog
                        alertDialog.setMessage("Please check your phone for a service fee payment of 10%");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("I have paid", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //volley to verify transaction
                                progressDialog.setMessage("verifying payment....");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                //fetch respose from api
                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.POST,
                                        confirm_payment_url,
                                        response -> {
                                            //check response returned

                                            switch (response){

                                                case "payment verified":
                                                    //go to success page
                                                    Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
                                                    finish();
                                                    intent.putExtra("owner_phone_number", owner_phone_number);
                                                    startActivity(intent);

                                                    break;

                                                case "payment unverified":
                                                    alertDialog.setTitle("Failed!");
                                                    alertDialog.setMessage("We could not verify you payment. Please try again\nYou need to pay a 10% service fee ");
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.setPositiveButton("Try again", (dialogInterface1, i1) -> {

                                                    });
                                                    alertDialog.show();

                                                    break;

                                            }


                                        }, error -> Toast.makeText(PaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()

                                ){
                                    //send params needed to db
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();

                                        params.put("pickupDate", pickupDate);
                                        params.put("pickupTime", pickupTime);
                                        params.put("vehicleTravelDestination", vehicleTravelDestination);
                                        params.put("returnDate", returnDate);
                                        params.put("returnTime", returnTime);
                                        params.put("phoneNumber", customerPhoneNumber);
                                        params.put("pickupLocation", pickupLocation);
                                        params.put("returnLocation", returnLocation);
                                        params.put("vehicle_id", vehicle_id);
                                        params.put("userEmail", user_email);
                                        params.put("price_per_day", String.valueOf(priceToPayLater));
                                        params.put("vehicle_owner_email", vehicle_owner_email);
                                        params.put("vehicle_title", vehicle_title);
                                        params.put("mpesa_payment_number", Utils.sanitizePhoneNumber(mpesaNumber));

                                        return params;

                                    }
                                };

                                Volley.newRequestQueue(PaymentActivity.this).add(stringRequest);


                            }
                        });

                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;

                            }
                        });

                    } else {
                        //if stk push was not successful
                        Toast.makeText(PaymentActivity.this, "Response %s" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("There seems to be an issue getting your payment. Please ensure your phone number is correct");

                    }
                    alertDialog.show();
                } catch (Exception e) {
                    Toast.makeText(PaymentActivity.this, "here" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PaymentActivity.this, "Mpesa failed to get you to pay! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                progressDialog.setMessage("Processing payment...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                paramsHash = new HashMap<>();
                paramsHash.put("amount", String.valueOf(1));
                paramsHash.put("nonce", strNonce);

                paramsHash.put("pickupDate", pickupDate);
                paramsHash.put("pickupTime", pickupTime);
                paramsHash.put("vehicleTravelDestination", vehicleTravelDestination);
                paramsHash.put("returnDate", returnDate);
                paramsHash.put("returnTime", returnTime);
                paramsHash.put("phoneNumber", owner_phone_number);
                paramsHash.put("pickupLocation", pickupLocation);
                paramsHash.put("returnLocation", returnLocation);
                paramsHash.put("vehicle_id", vehicle_id);
                paramsHash.put("userEmail", user_email);
                paramsHash.put("price_per_day", String.valueOf(priceToPayLater));
                paramsHash.put("vehicle_owner_email", vehicle_owner_email);
                paramsHash.put("vehicle_title", vehicle_title);

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


                            progressDialog.dismiss();
                            //transaction is successful
                            //go to success page
                            Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
                            finish();
                            intent.putExtra("owner_phone_number", owner_phone_number);
                            startActivity(intent);


                        }else{

                            //Transaction failed
                            alertDialog.setMessage("Transaction Failed! Please try again\n Response: " + response);
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialog.show();

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

        progressDialog.dismiss();

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