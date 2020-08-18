package com.example.kenyanrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountFragment extends Fragment {

    CardView cardListVehicles, cardBookings;
    EditText editTextFirstName, editTextSecondName, editTextId, editTextMobileNumber, editTextEmail, editTextCity, editTextCountry, editTextAddress;

    TextView txtSignOut, editTextDateOfBirth, txtViewTotalBookings ;
    
    Button btnSaveChanges;

    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;

    private int mYear, mMonth, mDay;

    private String dateOfBirth, userId;

    private List<VehicleBookings> vehicleBookingsList;

    String update_user_data = "https://kenyanrides.com/android/update_user_data.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_account, null);

        //initialize views
        editTextFirstName = myview.findViewById(R.id.edtxt_first_name);
        editTextSecondName = myview.findViewById(R.id.edtxt_second_name);
        editTextId = myview.findViewById(R.id.edtxt_id);
        editTextMobileNumber = myview.findViewById(R.id.edtxt_mobile_number);
        editTextEmail = myview.findViewById(R.id.edtxt_email_address);
        editTextDateOfBirth = myview.findViewById(R.id.edtxt_date_of_birth);
        editTextCity =myview.findViewById(R.id.edtxt_city);
        editTextCountry =myview.findViewById(R.id.edtxt_country);
        editTextAddress = myview.findViewById(R.id.edtxt_address);

        txtSignOut = myview.findViewById(R.id.txt_sign_out);
        
        btnSaveChanges =myview.findViewById(R.id.btnSaveChanges);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());


        //getting the current user
        user user = SharedPrefManager.getInstance(getActivity()).getUser();

        //setting the values to the textviews
        editTextId.setText(String.valueOf(user.getNational_id()));
        editTextFirstName.setText(user.getFirst_name());
        editTextSecondName.setText(user.getSecond_name());
        editTextMobileNumber.setText(user.getMobile_number());
        editTextEmail.setText(user.getEmail());
        editTextDateOfBirth.setText(user.getDob());
        editTextCity.setText(user.getCity());
        editTextCountry.setText(user.getCountry());
        editTextAddress.setText(user.getAddress());

        userId = String.valueOf(user.getId());

        cardListVehicles = myview.findViewById(R.id.card_list_vehicles);

        cardBookings = myview.findViewById(R.id.card_bookings);

        cardListVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ListVehiclesActivity.class);
                startActivity(intent);

            }
        });

        cardBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), BookingsActivity.class);
                intent.putExtra("user_email",user.getEmail());
                startActivity(intent);

            }
        });

        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (datePicker, i, i1, i2) -> {

                            Calendar selectedCal = Calendar.getInstance();
                            selectedCal.set(i, i1, i2);
                            Date selectedDate = selectedCal.getTime();
                            Date currentDate = Calendar.getInstance().getTime();
                            int diff1 = selectedDate.compareTo(currentDate);
                            if(diff1>0){
                                alertDialogBuilder.setMessage("Please enter a valid date");
                                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        editTextDateOfBirth.setText(user.getDob());
                                    }
                                });
                                alertDialogBuilder.show();
                                return;
                            }

                            editTextDateOfBirth.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            dateOfBirth = i2 + "/" + (i1 + 1) + "/" + i;

                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });
        
        btnSaveChanges.setOnClickListener(view -> updateUserData());


        //when the user presses signout text
        //calling the logout method
        txtSignOut.setOnClickListener(view -> {
            SharedPrefManager.getInstance(getActivity()).logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(intent);
        });

        return myview;

    }

    private void updateUserData() {

        //check if edittext are empty
        String firstName = editTextFirstName.getText().toString();
        if(TextUtils.isEmpty(firstName)){
            editTextFirstName.setError("First name cannot be empty");
            return;
        }

        String secondName = editTextSecondName.getText().toString();
        if(TextUtils.isEmpty(secondName)){
            editTextSecondName.setError("Second name cannot be empty");
            return;
        }

        String id = editTextId.getText().toString();
        if(TextUtils.isEmpty(id)){
            editTextId.setError("Please enter your ID number");
            return;
        }

        String phoneNumber = editTextMobileNumber.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            editTextMobileNumber.setError("Please enter your Phone Number");
            return;
        }

        String emailAddress = editTextEmail.getText().toString();
        if(TextUtils.isEmpty(emailAddress)){
            editTextEmail.setError("Please enter your Email Address");
            return;
        }

        String dob = editTextDateOfBirth.getText().toString();
        if(dob.equals("Date of Birth")){
            editTextDateOfBirth.setError("Please enter your date of birth");
            return;
        }

        String city = editTextCity.getText().toString();
        if(TextUtils.isEmpty(city)){
            editTextCity.setError("Please enter your City/Town");
            return;
        }

        String country = editTextCountry.getText().toString();
        if(TextUtils.isEmpty(country)){
            editTextCountry.setError("Please enter your Country");
            return;
        }

        String address = editTextAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            editTextAddress.setError("Please enter your Country");
            return;
        }

        dateOfBirth = editTextDateOfBirth.getText().toString();
        if(TextUtils.isEmpty(dateOfBirth)){
            editTextDateOfBirth.setError("Please enter your date of birth");
            return;
        }


        progressDialog.setMessage("Updating. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Login and fetch result from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                update_user_data,
                response -> {

                    //check response from api
                    switch (response){
                        case "Record updated successfully":
                            alertDialogBuilder.setMessage("Record updated successfully");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            });
                            alertDialogBuilder.show();

                            break;

                        case "Error updating record":
                            alertDialogBuilder.setMessage("Update not successful. Please try again");
                            alertDialogBuilder.show();

                            break;
                    }


                }, error -> {

                    progressDialog.dismiss();

                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("first_name", firstName);
                params.put("second_name", secondName);
                params.put("national_id", id);
                params.put("mobile_number", phoneNumber);
                params.put("email_address", emailAddress);
                params.put("dob", dateOfBirth);
                params.put("city", city);
                params.put("country", country);
                params.put("address", address);
                params.put("user_id", userId);

                return params;

            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

}
