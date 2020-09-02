package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private TextView TxtPickupDate, TxtPickupTime, TxtReturnDate, TxtReturnTime;

    private EditText editTextMpesaNumber;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String vehicleTravelLocation;
    String mpesaNumber;
    String pickupLocation;
    String returnLocation;
    String pickUpDate;
    String pickUpTime;
    String returnDate;
    String returnTime;

    Date selectedPickupDate;
    Date selectedReturnDate;

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;

    private Button btnBookNow;

    private String vehicle_id,user_email, price_per_day, vehicle_owner_email, vehicle_title, owner_phone_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

        //get strings from intent
        vehicle_id = getIntent().getStringExtra("vehicle_id");
        user_email = getIntent().getStringExtra("user_email");
        price_per_day = getIntent().getStringExtra("price_per_day");
        vehicle_owner_email = getIntent().getStringExtra("vehicle_owner_email");
        owner_phone_number = getIntent().getStringExtra("owner_phone_number");
        vehicle_title = getIntent().getStringExtra("vehicle_title");


        //initializing views
        TxtPickupDate = findViewById(R.id.pickupDate);
        TxtPickupTime = findViewById(R.id.pickupTime);
        TxtReturnDate = findViewById(R.id.returnDate);
        TxtReturnTime = findViewById(R.id.returnTime);

        editTextMpesaNumber = findViewById(R.id.edit_text_mpesa_number);

        btnBookNow = findViewById(R.id.btnBookNow);

        TxtPickupDate.setOnClickListener(this);
        TxtPickupTime.setOnClickListener(this);
        TxtReturnDate.setOnClickListener(this);
        TxtReturnTime.setOnClickListener(this);

        btnBookNow.setOnClickListener(this);

        //places autocomplete
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(this.getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the vehicle travel destination location.
        AutocompleteSupportFragment travel_destination_fragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.travel_destination);
        travel_destination_fragment.setCountry("KE");
        travel_destination_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        travel_destination_fragment.setHint("Travel Destination");
        travel_destination_fragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        travel_destination_fragment.getView().findViewById(R.id.places_autocomplete_clear_button).setVisibility(View.GONE);

        //customise autocomplete edittext
        EditText travel_destination_place = travel_destination_fragment.getView().findViewById(R.id.places_autocomplete_search_input);
        travel_destination_place.setTextSize(12.0f);
        travel_destination_place.setHintTextColor(getResources().getColor(R.color.black));
        travel_destination_place.setGravity(Gravity.CENTER_VERTICAL);
        travel_destination_place.setBackground(getResources().getDrawable(R.drawable.input_shape));
        travel_destination_place.setPadding(15,25,15,60);

        travel_destination_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                vehicleTravelLocation = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(BookNowActivity.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize the pickup location.
        AutocompleteSupportFragment pickup_location_fragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.pickup_location);
        pickup_location_fragment.setCountry("KE");
        pickup_location_fragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        pickup_location_fragment.setHint("SELECT");
        pickup_location_fragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        pickup_location_fragment.getView().findViewById(R.id.places_autocomplete_clear_button).setVisibility(View.GONE);

        //customise autocomplete edittext
        EditText etPlace = pickup_location_fragment.getView().findViewById(R.id.places_autocomplete_search_input);
        etPlace.setTextSize(12.0f);
        etPlace.setHintTextColor(getResources().getColor(R.color.black));
        etPlace.setGravity(Gravity.CENTER_VERTICAL);
        etPlace.setBackground(getResources().getDrawable(R.drawable.input_shape));
        etPlace.setPadding(15,25,15,60);

        pickup_location_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                pickupLocation = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(BookNowActivity.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the return location fragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.return_location);
        autocompleteFragment.setCountry("KE");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocompleteFragment.setHint("SELECT");
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setVisibility(View.GONE);

        //customise autocomplete edittext
        EditText editTextPlace = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        editTextPlace.setTextSize(12.0f);
        editTextPlace.setHintTextColor(getResources().getColor(R.color.black));
        editTextPlace.setGravity(Gravity.CENTER_VERTICAL);
        editTextPlace.setBackground(getResources().getDrawable(R.drawable.input_shape));
        editTextPlace.setPadding(15,25,15,60);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                returnLocation = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(BookNowActivity.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog = new AlertDialog.Builder(this);

        progressDialog = new ProgressDialog(this);




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
                    (datePicker, i, i1, i2) -> {

                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(i, i1, i2);
                        selectedPickupDate = selectedCal.getTime();
                        Date currentDate = Calendar.getInstance().getTime();
                        int diff1 = currentDate.compareTo(selectedPickupDate);
                        if(diff1 > 0){
                            alertDialog.setMessage("Please enter a valid date");
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("OK", (dialogInterface, i3) -> TxtPickupDate.setText("Pickup Date"));
                            alertDialog.setNegativeButton("Cancel", (dialogInterface, i32) -> TxtPickupDate.setText("Pickup Date"));
                            alertDialog.show();
                            return;
                        }

                        TxtPickupDate.setText(i2 + "/" + (i1 + 1) + "/" + i);


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

            //check if user has already entered pickup date
            if (selectedPickupDate != null) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                Calendar selectedCal = Calendar.getInstance();
                                selectedCal.set(i, i1, i2);
                                selectedReturnDate = selectedCal.getTime();


                                int diff1 = selectedPickupDate.compareTo(selectedReturnDate);
                                if (diff1 > 0) {
                                    alertDialog.setMessage("Please enter a date that is greater than the date you've set for pick up");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("OK", (dialogInterface, i3) -> TxtReturnDate.setText("Return Date"));
                                    alertDialog.setNegativeButton("Cancel", (dialogInterface, i32) -> TxtReturnDate.setText("Return Date"));
                                    alertDialog.show();
                                    return;
                                }

                                TxtReturnDate.setText(i2 + "/" + (i1 + 1) + "/" + i);


                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }else {
                Toast.makeText(BookNowActivity.this, "Please enter the date you will pick up the car first ", Toast.LENGTH_LONG).show();
            }

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
        if (vehicleTravelLocation.isEmpty()){
            Toast.makeText(this, "Please enter your travel destination", Toast.LENGTH_SHORT).show();
            return;
        }

        mpesaNumber = editTextMpesaNumber.getText().toString();
        if(TextUtils.isEmpty(mpesaNumber)){
            editTextMpesaNumber.setError("Please enter your Phone Number that the owner will contact you with");
            return;
        }

        if (pickupLocation.isEmpty()){
            Toast.makeText(this, "Please select location of the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        if (returnLocation.isEmpty()){
            Toast.makeText(this, "Please select location of the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        pickUpDate = TxtPickupDate.getText().toString();

        if (pickUpDate.equals("Pickup Date")){
            TxtPickupDate.setError("Please select a date you'll pickup the vehicle");
            return;
        }

        pickUpTime = TxtPickupTime.getText().toString();

        if (pickUpTime.equals("Pickup Time")){
            TxtPickupTime.setError("Please select a Time you'll pickup the vehicle");
            return;
        }

        returnDate = TxtReturnDate.getText().toString();

        if (returnDate.equals("Pickup Date")){
            TxtReturnDate.setError("Please select a date you'll return the vehicle");
            return;
        }

        returnTime = TxtReturnTime.getText().toString();

        if (returnTime.equals("Pickup Date")){
            TxtReturnTime.setError("Please select a time you'll return the vehicle");
            return;
        }

        Intent intent = new Intent(BookNowActivity.this, PaymentActivity.class);

        //send car info to payment activity
        intent.putExtra("vehicle_id", vehicle_id);
        intent.putExtra("user_email", user_email);
        intent.putExtra("price_per_day", price_per_day);
        intent.putExtra("vehicle_owner_email", vehicle_owner_email);
        intent.putExtra("vehicle_title", vehicle_title);

        intent.putExtra("pickupDate", pickUpDate);
        intent.putExtra("pickupTime", pickUpTime);
        intent.putExtra("vehicleTravelDestination", vehicleTravelLocation);
        intent.putExtra("returnDate", returnDate);
        intent.putExtra("returnTime", returnTime);
        intent.putExtra("customerPhoneNumber", mpesaNumber);
        intent.putExtra("pickupLocation", pickupLocation);
        intent.putExtra("returnLocation", returnLocation);
        intent.putExtra("vehicle_id", vehicle_id);
        intent.putExtra("owner_phone_number", owner_phone_number);

        startActivity(intent);


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}