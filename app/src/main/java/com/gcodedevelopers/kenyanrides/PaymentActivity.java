package com.gcodedevelopers.kenyanrides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import Model.AccessToken;
import Model.STKPush;
import Services.DarajaApiClient;
import Services.Utils;
import retrofit2.Call;
import retrofit2.Callback;

import static com.gcodedevelopers.kenyanrides.Constants.BUSINESS_SHORT_CODE;
import static com.gcodedevelopers.kenyanrides.Constants.CALLBACKURL;
import static com.gcodedevelopers.kenyanrides.Constants.PARTYB;
import static com.gcodedevelopers.kenyanrides.Constants.PASSKEY;
import static com.gcodedevelopers.kenyanrides.Constants.TRANSACTION_TYPE;

public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String URL_GET_TOKEN = "https://kenyanrides.com/android/braintree/main.php";
    final String URL_CHECKOUT = "https://kenyanrides.com/android/braintree/checkout.php";

    private Button payWithMpesaButton, payWithCardButton;

    private LinearLayout layoutMpesa, layoutCreditCard, layoutPesaPalPaymentForm;

    private TextInputLayout mpesaNumberInput;

    private ProgressDialog progressDialog;

    private AlertDialog.Builder alertDialog;

    private String paymentFirstName, paymentLastName, paymentPhoneNumber, paymentEmailAddress;

    private Map<String, String> paramsHash;

    private String vehicle_id, user_email, price_per_day, vehicle_owner_email, vehicle_title, pickupDate, pickupTime, vehicleTravelDestination,
            returnDate, returnTime, customerPhoneNumber, pickupLocation, returnLocation, mpesaNumber, owner_phone_number;

    private int priceToPayNow, priceToPayLater;

    private EditText editTextMpesaNumber, editTextFirstName, editTextLastName, editTextEmailAddress, editTextPhoneNumber;

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
        payWithMpesaButton = findViewById(R.id.payWithMpesaButton);

        payWithCardButton = findViewById(R.id.payWithCardButton);

        layoutPesaPalPaymentForm = findViewById(R.id.pesapal_payment_form);

        alertDialogBuilder = new AlertDialog.Builder(this);

        layoutCreditCard = findViewById(R.id.layout_credit_card);

        layoutMpesa = findViewById(R.id.layout_mpesa);

        mpesaNumberInput = findViewById(R.id.mpesa_number);

        editTextMpesaNumber = findViewById(R.id.edit_Text_mpesa_number);

        textViewTotalPrice = findViewById(R.id.text_view_total_price);

        textViewServiceFee = findViewById(R.id.text_view_service_fee);

        textViewRemainingAmount = findViewById(R.id.text_view_remaining_amount);

        editTextFirstName = findViewById(R.id.edit_text_payment_first_name);

        editTextLastName = findViewById(R.id.edit_text_payment_last_name);

        editTextPhoneNumber = findViewById(R.id.edit_text_payment_phone_number);

        editTextEmailAddress = findViewById(R.id.edit_text_payment_email_address);


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
                layoutPesaPalPaymentForm.setVisibility(View.GONE);
                mpesaNumberInput.setVisibility(View.VISIBLE);
                payWithMpesaButton.setVisibility(View.VISIBLE);
                payWithCardButton.setVisibility(View.GONE);
            }
        });

        layoutCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutCreditCard.setBackgroundColor(Color.parseColor("#C2DEEF"));
                layoutMpesa.setBackgroundColor(getResources().getColor(android.R.color.white));

                //show layout to get pesapal required details and disable mpesa number option
                mpesaNumberInput.setVisibility(View.GONE);
                layoutPesaPalPaymentForm.setVisibility(View.VISIBLE);
                payWithMpesaButton.setVisibility(View.GONE);
                payWithCardButton.setVisibility(View.VISIBLE);

            }
        });

        //set text of button
        payWithMpesaButton.setText("Pay Ksh " + priceToPayNow + " with M-Pesa");

        payWithCardButton.setText("Pay Ksh " + priceToPayNow + " with card");

        payWithMpesaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pay with mpesa

                //check if number has been entered
                mpesaNumber = editTextMpesaNumber.getText().toString();
                if(TextUtils.isEmpty(mpesaNumber)){
                    editTextMpesaNumber.setError("Please enter your Mpesa number that you will pay with");
                    return;
                } else{

                    //mpesa implementation
                    performSTKPush(mpesaNumber, String.valueOf(priceToPayNow));

                }


            }
        });

        //pesapal implementation
        payWithCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if all fields are filled
                paymentFirstName = editTextFirstName.getText().toString();
                if(TextUtils.isEmpty(paymentFirstName)){
                    editTextFirstName.setError("Please enter your First Name to proceed");
                    return;
                }

                paymentLastName = editTextLastName.getText().toString();
                if(TextUtils.isEmpty(paymentLastName)){
                    editTextLastName.setError("Please enter your Last Name to proceed");
                    return;
                }

                paymentEmailAddress = editTextEmailAddress.getText().toString();
                if(TextUtils.isEmpty(paymentEmailAddress)){
                    editTextEmailAddress.setError("Please enter your Email Address to proceed");
                    return;
                }

                paymentPhoneNumber = editTextPhoneNumber.getText().toString();
                if(TextUtils.isEmpty(paymentPhoneNumber)){
                    editTextPhoneNumber.setError("Please enter your Phone Number to proceed");
                    return;
                }

                if (!isNetworkAvailable()){
                    //if the user is not connected to the internet
                    alertDialogBuilder.setTitle("Network Failure");
                    alertDialogBuilder.setMessage("Please check your internet connection!");
                    alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                    return;

                }else {

                    //go to pesapal portal to initialize payment and send customer information
                    Intent intent = new Intent(PaymentActivity.this, PesaPalPayment.class);

                    intent.putExtra("first_name", paymentFirstName);
                    intent.putExtra("last_name", paymentLastName);
                    intent.putExtra("email_address", paymentEmailAddress);
                    intent.putExtra("phone_number", paymentPhoneNumber);
                    intent.putExtra("amount", String.valueOf(priceToPayNow));
                    intent.putExtra("vehicle_title", vehicle_title);

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
                    intent.putExtra("price_to_pay_later", String.valueOf(priceToPayLater));


                    startActivity(intent);

                }

            }
        });


        progressDialog = new ProgressDialog(this);

        alertDialog = new AlertDialog.Builder(this);

        //mpesa token

        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

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
                "Progressive Fibre ltd", //Account reference
                "Progressive Fibre ltd"  //Transaction description
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

                                //fetch response from api
                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.POST,
                                        confirm_payment_url,
                                        response -> {
                                            //check response returned
                                            switch (response){

                                                case "payment verified":
                                                    //go to success page
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
                                                    finish();
                                                    intent.putExtra("owner_phone_number", owner_phone_number);
                                                    startActivity(intent);

                                                    break;

                                                case "payment unverified":
                                                    progressDialog.dismiss();
                                                    alertDialog.setTitle("Failed!");
                                                    alertDialog.setMessage("We could not verify you payment. Please try again\nYou need to pay a 10% service fee ");
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.setPositiveButton("Try again", (dialogInterface1, i1) -> {

                                                    });
                                                    alertDialog.show();

                                                    break;

                                                default:
                                                    Intent intent2 = new Intent(PaymentActivity.this, SuccessActivity.class);
                                                    finish();
                                                    intent2.putExtra("owner_phone_number", owner_phone_number);
                                                    startActivity(intent2);
                                                    break;

                                            }


                                        }, error -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(PaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }

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
                                        params.put("service_fee", String.valueOf(priceToPayNow));

                                        return params;

                                    }
                                };

                                Volley.newRequestQueue(PaymentActivity.this).add(stringRequest);


                            }
                        });

                        alertDialog.setNegativeButton("Change Payment Method", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;

                            }
                        });

                    } else {
                        //if stk push was not successful
                        Toast.makeText(PaymentActivity.this, "Response %s" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("There seems to be an issue getting your payment. ");

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}