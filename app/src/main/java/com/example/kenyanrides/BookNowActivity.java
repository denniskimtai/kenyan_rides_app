package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Model.AccessToken;
import Model.STKPush;

import Services.DarajaApiClient;
import Services.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.kenyanrides.Constants.BUSINESS_SHORT_CODE;
import static com.example.kenyanrides.Constants.CALLBACKURL;
import static com.example.kenyanrides.Constants.PARTYB;
import static com.example.kenyanrides.Constants.PASSKEY;
import static com.example.kenyanrides.Constants.TRANSACTION_TYPE;



public class BookNowActivity extends AppCompatActivity implements View.OnClickListener {

    public static DrawerLayout drawer;

    private TextView TxtPickupDate, TxtPickupTime, TxtReturnDate, TxtReturnTime;

    private EditText editTextVehicleTravelLocation, editTextMpesaNumber, editTextPickupLocation, editTextReturnLocation;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private AlertDialog.Builder alertDialog;

    private Button btnBookNow;

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

        //initializing views
        TxtPickupDate = findViewById(R.id.pickupDate);
        TxtPickupTime = findViewById(R.id.pickupTime);
        TxtReturnDate = findViewById(R.id.returnDate);
        TxtReturnTime = findViewById(R.id.returnTime);

        editTextVehicleTravelLocation = findViewById(R.id.edit_text_vehicle_travel_destination);
        editTextMpesaNumber = findViewById(R.id.edit_text_mpesa_number);
        editTextPickupLocation = findViewById(R.id.edit_text_pickup_location);
        editTextReturnLocation = findViewById(R.id.edit_text_return_location);

        btnBookNow = findViewById(R.id.btnBookNow);

        TxtPickupDate.setOnClickListener(this);
        TxtPickupTime.setOnClickListener(this);
        TxtReturnDate.setOnClickListener(this);
        TxtReturnTime.setOnClickListener(this);

        btnBookNow.setOnClickListener(this);



        alertDialog = new AlertDialog.Builder(this);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();


    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        if (view == TxtPickupDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            TxtPickupDate.setText(i2 + "/" + (i1 + 1) + "/" + i);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }


        if (view == TxtPickupTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {


                            TxtPickupTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();


        }

        if (view == TxtReturnDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            TxtReturnDate.setText(i2 + "/" + (i1 + 1) + "/" + i);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }

        if (view == TxtReturnTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            TxtReturnTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if(view == btnBookNow){

            BookCar();

        }

    }

    private void BookCar() {

        //check if every field has been field
        String vehicleTravelLocation = editTextVehicleTravelLocation.getText().toString();
        if(TextUtils.isEmpty(vehicleTravelLocation)){
            editTextVehicleTravelLocation.setError("Please enter your travel destination");
            return;
        }

        String mpesaNumber = editTextMpesaNumber.getText().toString();
        if(TextUtils.isEmpty(mpesaNumber)){
            editTextMpesaNumber.setError("Please enter your Mpesa number that you will pay with");
            return;
        }

        String pickupLocation = editTextPickupLocation.getText().toString();
        if(TextUtils.isEmpty(pickupLocation)){
            editTextPickupLocation.setError("Please enter a pickup location");
            return;
        }

        String returnLocation = editTextReturnLocation.getText().toString();
        if(TextUtils.isEmpty(returnLocation)){
            editTextReturnLocation.setError("Please enter a return location");
            return;
        }

        //mpesa implementation
        performSTKPush(mpesaNumber,"1");

    }

    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
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
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookNowActivity.this, "post submitted to API. %s" + response.body(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(BookNowActivity.this, "Response %s"+ response.errorBody(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(BookNowActivity.this, "here" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(BookNowActivity.this, "hapa "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}