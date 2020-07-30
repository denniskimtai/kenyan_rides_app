package com.example.kenyanrides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private CarsAdapter carsAdapter;
    private List<car> carList;

    private static final String vehicles_url = "https://kenyanrides.com/android/api.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, null);

        recyclerView = myView.findViewById(R.id.cars_recycler_view);


        carList = new ArrayList<>();



        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);



        prepareCars();



        return myView;


    }

    private void prepareCars() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, vehicles_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray carsJson = new JSONArray(response);

                    for(int i = 0; i<carsJson.length(); i++) {

                        //get json objects
                        JSONObject carsObject = carsJson.getJSONObject(i);

                        String vehicleTitle = carsObject.getString("VehiclesTitle");

                        int pricePerDay = carsObject.getInt("PricePerDay");

                        String vehicleImage = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage1");


                        //
                        String vehicleBrand = carsObject.getString("VehiclesBrand");
                        String vehicleOverview = carsObject.getString("VehiclesOverview");
                        String poweredBy = carsObject.getString("poweredby");
                        String fuelType = carsObject.getString("FuelType");
                        String modelYear = carsObject.getString("ModelYear");
                        String seatingCapacity = carsObject.getString("SeatingCapacity");
                        String driverStatus = carsObject.getString("Dstatus");
                        String vehicleImage2 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage2");
                        String vehicleImage3 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage3");
                        String vehicleImage4 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage4");
                        String vehicleImage5 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage5");
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

                        car car = new car(vehicleImage, vehicleTitle, pricePerDay, vehicleBrand,
                                vehicleOverview, poweredBy, fuelType, modelYear, seatingCapacity,
                                driverStatus, vehicleImage2, vehicleImage3, vehicleImage4, vehicleImage5,
                                airConditioner, powerDoorLocks, antiLockBrakingSystem, brakeAssist, powerSteering,
                                driverAirbag, passengerAirbag, powerWindows, cdPlayer, centralLocking,
                                crashSensor, leatherSeats, ownerId, regDate, booked);

                        carList.add(car);



                    }

                    carsAdapter = new CarsAdapter(getActivity(), carList);
                    recyclerView.setAdapter(carsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }
}
