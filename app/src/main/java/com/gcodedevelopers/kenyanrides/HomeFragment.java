package com.gcodedevelopers.kenyanrides;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout linearLayoutEmpty;
    private CarsAdapter carsAdapter;
    private List<car> carList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private EditText editTextSearch;

    private static final String vehicles_url = "https://kenyanrides.com/android/fetch_api.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, null);

        editTextSearch = myView.findViewById(R.id.searchBar);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });

        recyclerView = myView.findViewById(R.id.cars_recycler_view);

        linearLayoutEmpty = myView.findViewById(R.id.linearEmpty);

        carList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);


        progressDialog = new ProgressDialog(getActivity());

        alertDialogBuilder = new AlertDialog.Builder(getActivity());



        prepareCars();

        final SwipeRefreshLayout pullToRefresh = myView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carList.clear();
                prepareCars();
                pullToRefresh.setRefreshing(false);
            }
        });



        return myView;


    }


    private void filter(String searchText){

        ArrayList<car> filteredList = new ArrayList<>();

        for(car item : carList){

            if (item.getCarName().toLowerCase().contains(searchText.toLowerCase())){

                filteredList.add(item);

            }

        }
        carsAdapter.filteredList(filteredList);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void prepareCars() {

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //check if network is connected
        if (!isNetworkAvailable()){
            progressDialog.dismiss();

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    prepareCars();

                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, vehicles_url, response -> {

            try {
                JSONArray carsJson = new JSONArray(response);

                //check if no cars are available
                if(carsJson.length() != 0){

                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayoutEmpty.setVisibility(View.GONE);

                    for(int i = 0; i<carsJson.length(); i++) {

                        //get json objects
                        JSONObject carsObject = carsJson.getJSONObject(i);

                        String vehicleTitle = carsObject.getString("VehiclesTitle");

                        int pricePerDay = carsObject.getInt("PricePerDay");

                        int id = carsObject.getInt("id");
                        String vehicleBrand = carsObject.getString("brandName");
                        String vehicleOverview = carsObject.getString("VehiclesOverview");
                        String poweredBy = carsObject.getString("poweredby");
                        String fuelType = carsObject.getString("FuelType");
                        String modelYear = carsObject.getString("ModelYear");
                        String seatingCapacity = carsObject.getString("SeatingCapacity");
                        String driverStatus = carsObject.getString("Dstatus");
                        String vehicleImage = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage1");
                        String image1Editted = vehicleImage.replace(" ", "%20");
                        String vehicleImage2 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage2");
                        String image2Editted = vehicleImage2.replace(" ", "%20");
                        String vehicleImage3 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage3");
                        String image3Editted = vehicleImage3.replace(" ", "%20");
                        String vehicleImage4 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage4");
                        String image4Editted = vehicleImage4.replace(" ", "%20");
                        String vehicleImage5 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage5");
                        String image5Editted = vehicleImage5.replace(" ", "%20");
                        String airConditioner = carsObject.getString("AirConditioner");
                        String powerDoorLocks = carsObject.getString("PowerDoorLocks");
                        String antiLockBrakingSystem = carsObject.getString("AntiLockBrakingSystem");
                        String brakeAssist = carsObject.getString("BrakeAssist");
                        String powerSteering = carsObject.getString("PowerSteering");
                        String driverAirbag = carsObject.getString("DriverAirbag");
                        String passengerAirbag = carsObject.getString("PassengerAirbag");
                        String powerWindows = carsObject.getString("PowerWindows");
                        String cdPlayer = carsObject.getString("CDPlayer");
                        String centralLocking = carsObject.getString("CentralLocking");
                        String crashSensor = carsObject.getString("CrashSensor");
                        String leatherSeats = carsObject.getString("LeatherSeats");
                        String ownerId = carsObject.getString("owner_id");
                        String regDate = carsObject.getString("RegDate");
                        String booked = carsObject.getString("booked");
                        String owner_phone_number = carsObject.getString("owner_phonenumber");
                        String car_video = carsObject.getString("car_video");


                        car car = new car(image1Editted, vehicleTitle, pricePerDay, id, vehicleBrand,
                                vehicleOverview, poweredBy, fuelType, modelYear, seatingCapacity,
                                driverStatus, image2Editted, image3Editted, image4Editted, image5Editted,
                                airConditioner, powerDoorLocks, antiLockBrakingSystem, brakeAssist, powerSteering,
                                driverAirbag, passengerAirbag, powerWindows, cdPlayer, centralLocking,
                                crashSensor, leatherSeats, ownerId, regDate, booked, owner_phone_number, car_video);

                        carList.add(car);



                    }

                }else {
                    recyclerView.setVisibility(View.GONE);
                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                }


                progressDialog.dismiss();

                carsAdapter = new CarsAdapter(getActivity(), carList);
                recyclerView.setAdapter(carsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                alertDialogBuilder.setTitle("Error occurred");
                alertDialogBuilder.setMessage("Please ensure you have stable internet connection!");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        prepareCars();
                    }
                });

                alertDialogBuilder.show();

            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }
}
