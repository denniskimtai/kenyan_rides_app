package com.example.kenyanrides;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListVehiclesActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private LinearLayout linearLayoutEmpty;
    private ListedVehiclesAdapter adapter;
    private List<ListVehicle> listedVehiclesList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private final String listedVehiclesUrl = "https://kenyanrides.com/android/listed_vehicles.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vehicles);

        recyclerView = (RecyclerView) findViewById(R.id.vehiclesListRecyclerView);
        linearLayoutEmpty = findViewById(R.id.linearEmpty);

        listedVehiclesList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);

        progressDialog = new ProgressDialog(this);

        prepareListedVehicles();

        

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void prepareListedVehicles() {

        //check if network is connected
        if (!isNetworkAvailable()){
            progressDialog.dismiss();

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    prepareListedVehicles();

                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
            return;
        }

        alertDialogBuilder = new AlertDialog.Builder(this);

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //getting the current user
        user user = SharedPrefManager.getInstance(this).getUser();

        String user_email = user.getEmail();

        //fetch from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                listedVehiclesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //fetch json object returned by api
                        try {
                            JSONArray listedVehiclesJson = new JSONArray(response);

                            if (listedVehiclesJson.length() == 0){

                                recyclerView.setVisibility(View.GONE);
                                linearLayoutEmpty.setVisibility(View.VISIBLE);

                            }else {
                                recyclerView.setVisibility(View.VISIBLE);
                                linearLayoutEmpty.setVisibility(View.GONE);

                                for (int i = 0; i < listedVehiclesJson.length(); i++) {

                                    //get json objects
                                    JSONObject listedVehiclesObject = listedVehiclesJson.getJSONObject(i);

                                    String vehicleTitle = listedVehiclesObject.getString("VehiclesTitle");

                                    int pricePerDay = listedVehiclesObject.getInt("PricePerDay");

                                    String vehicleImage = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + listedVehiclesObject.getString("Vimage1");


                                    //
                                    int id = listedVehiclesObject.getInt("id");
                                    String vehicleBrand = listedVehiclesObject.getString("VehiclesBrand");
                                    String vehicleOverview = listedVehiclesObject.getString("VehiclesOverview");
                                    String poweredBy = listedVehiclesObject.getString("poweredby");
                                    String fuelType = listedVehiclesObject.getString("FuelType");
                                    String modelYear = listedVehiclesObject.getString("ModelYear");
                                    String seatingCapacity = listedVehiclesObject.getString("SeatingCapacity");
                                    String driverStatus = listedVehiclesObject.getString("Dstatus");
                                    String vehicleImage2 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + listedVehiclesObject.getString("Vimage2");
                                    String vehicleImage3 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + listedVehiclesObject.getString("Vimage3");
                                    String vehicleImage4 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + listedVehiclesObject.getString("Vimage4");
                                    String vehicleImage5 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + listedVehiclesObject.getString("Vimage5");
                                    String airConditioner = listedVehiclesObject.getString("AirConditioner");
                                    String powerDoorLocks = listedVehiclesObject.getString("PowerDoorLocks");
                                    String antiLockBrakingSystem = listedVehiclesObject.getString("AntiLockBrakingSystem");
                                    String brakeAssist = listedVehiclesObject.getString("BrakeAssist");
                                    String powerSteering = listedVehiclesObject.getString("PowerSteering");
                                    String driverAirbag = listedVehiclesObject.getString("DriverAirbag");
                                    String passengerAirbag = listedVehiclesObject.getString("PassengerAirbag");
                                    String powerWindows = listedVehiclesObject.getString("PowerWindows");
                                    String cdPlayer = listedVehiclesObject.getString("CDPlayer");
                                    String centralLocking = listedVehiclesObject.getString("CentralLocking");
                                    String crashSensor = listedVehiclesObject.getString("CrashSensor");
                                    String leatherSeats = listedVehiclesObject.getString("LeatherSeats");
                                    String ownerId = listedVehiclesObject.getString("owner_id");
                                    String regDate = listedVehiclesObject.getString("RegDate");
                                    String booked = listedVehiclesObject.getString("booked");

                                    ListVehicle listVehicle = new ListVehicle(vehicleImage, vehicleTitle, pricePerDay, id, vehicleBrand,
                                            vehicleOverview, poweredBy, fuelType, modelYear, seatingCapacity,
                                            driverStatus, vehicleImage2, vehicleImage3, vehicleImage4, vehicleImage5,
                                            airConditioner, powerDoorLocks, antiLockBrakingSystem, brakeAssist, powerSteering,
                                            driverAirbag, passengerAirbag, powerWindows, cdPlayer, centralLocking,
                                            crashSensor, leatherSeats, ownerId, regDate, booked);

                                    listedVehiclesList.add(listVehicle);

                                }

                            }

                            progressDialog.dismiss();

                            adapter = new ListedVehiclesAdapter(ListVehiclesActivity.this, listedVehiclesList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ListVehiclesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", user_email);

                return params;

            }
        };

        Volley.newRequestQueue(this).add(stringRequest);




    }


}
