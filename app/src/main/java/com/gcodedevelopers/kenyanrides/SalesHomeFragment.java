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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

public class SalesHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout linearLayoutEmpty;
    private salesCarsAdapter carsAdapter;
    private List<SalesCar> carList;

    private ImageView iconFilter;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private EditText editTextSearch;

    private static final String sales_vehicles_url = "https://kenyanrides.com/android/fetch_vehicles_on_sale.php";

    private String[] carTypes = {"SELECT","SALOON","SUV","PSV","COMMERCIAL","OTHERS"};
    int carTypeCode;
    String minPrice;
    String maxPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_sales_home, null);

        if( getArguments() != null) {
            String car_category = getArguments().getString("selected_car_category");
            Toast.makeText(getActivity(), car_category, Toast.LENGTH_SHORT).show();
        }

        editTextSearch = myView.findViewById(R.id.searchBar);

        iconFilter = myView.findViewById(R.id.icon_filter);

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

        iconFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFilterDialog();

            }
        });

        prepareCars();

        final SwipeRefreshLayout pullToRefresh = myView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            carList.clear();
            prepareCars();
            pullToRefresh.setRefreshing(false);
        });

        return myView;
    }

    private void showFilterDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.filter_dialog_layout, null);

        Spinner carTypeSpinner = view.findViewById(R.id.simpleSpinner);
        EditText editTextMinPrice = view.findViewById(R.id.edt_min_price);
        EditText editTextMaxPrice = view.findViewById(R.id.edt_max_price);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, carTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carTypeSpinner.setAdapter(adapter);

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (carTypeSpinner.getSelectedItem().toString()){

                    case "SALOON":
                        carTypeCode = 1;
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;

                    case "SUV":
                        carTypeCode = 2;
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;

                    case "PSV":
                        carTypeCode = 3;
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;

                    case "COMMERCIAL":
                        carTypeCode = 4;
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;

                    case "OTHERS":
                        carTypeCode = 5;
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;

                    default:
                        minPrice = editTextMinPrice.getText().toString().trim();
                        maxPrice = editTextMaxPrice.getText().toString().trim();
                        carListFilter(carTypeCode,minPrice,maxPrice);
                        break;
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(view);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        //Set negative button text color
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

        LinearLayout.LayoutParams nButtonLL = (LinearLayout.LayoutParams) nbutton.getLayoutParams();
        nButtonLL.gravity = Gravity.LEFT;
        nbutton.setLayoutParams(nButtonLL);

        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set negative button text color
        pbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

    }


    private void priceFilter(String minPrice, String maxPrice){

        if (!TextUtils.isEmpty(minPrice) && TextUtils.isEmpty(maxPrice)){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }else if (TextUtils.isEmpty(minPrice) && !TextUtils.isEmpty(maxPrice)){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()<= Integer.parseInt(maxPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }else{

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice) && item.getCarPrice()<= Integer.parseInt(maxPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }

    }

    private void carListFilter(int categoryCode, String minPrice, String maxPrice){

        //min price is the only field with value
        if(!TextUtils.isEmpty(minPrice) && TextUtils.isEmpty(maxPrice) && TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);


        }
        //only max price has a value
        else if(TextUtils.isEmpty(minPrice) && !TextUtils.isEmpty(maxPrice) && TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()<= Integer.parseInt(maxPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }
        //only search category has a value
        else if (TextUtils.isEmpty(minPrice) && TextUtils.isEmpty(maxPrice) && !TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getVehicle_category().equals(String.valueOf(categoryCode))){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }
        //only min and max price have a value
        else if (!TextUtils.isEmpty(minPrice) && !TextUtils.isEmpty(maxPrice) && TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice) && item.getCarPrice()<= Integer.parseInt(maxPrice)){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);


        }
        //only min price and category have a value
        else if (!TextUtils.isEmpty(minPrice) && TextUtils.isEmpty(maxPrice) && !TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice) && item.getVehicle_category().equals(String.valueOf(categoryCode))){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }//only max price and category have a value
        else if (TextUtils.isEmpty(minPrice) && !TextUtils.isEmpty(maxPrice) && !TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()<= Integer.parseInt(maxPrice) && item.getVehicle_category().equals(String.valueOf(categoryCode))){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }//all have a value
        else if (!TextUtils.isEmpty(minPrice) && !TextUtils.isEmpty(maxPrice) && !TextUtils.isEmpty(String.valueOf(categoryCode))){

            ArrayList<SalesCar> filteredList = new ArrayList<>();

            for(SalesCar item : carList){

                if (item.getCarPrice()>= Integer.parseInt(minPrice) && item.getCarPrice()<= Integer.parseInt(maxPrice) && item.getVehicle_category().equals(String.valueOf(categoryCode))){

                    filteredList.add(item);

                }

            }
            carsAdapter.filteredList(filteredList);

        }




    }


    private void filter(String searchText){

        ArrayList<SalesCar> filteredList = new ArrayList<>();

        for(SalesCar item : carList){

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

        progressDialog.setMessage("Loading.Please wait...");
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, sales_vehicles_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                            String vehicle_category = carsObject.getString("vehicle_category");

                            SalesCar salesCar = new SalesCar(image1Editted, vehicleTitle, pricePerDay, id, vehicleBrand,
                                    vehicleOverview, poweredBy, fuelType, modelYear, seatingCapacity,
                                    driverStatus, image2Editted, image3Editted, image4Editted, image5Editted,
                                    airConditioner, powerDoorLocks, antiLockBrakingSystem, brakeAssist, powerSteering,
                                    driverAirbag, passengerAirbag, powerWindows, cdPlayer, centralLocking,
                                    crashSensor, leatherSeats, ownerId, regDate, booked, owner_phone_number, car_video, vehicle_category);

                            carList.add(salesCar);

                        }

                    }else {
                        recyclerView.setVisibility(View.GONE);
                        linearLayoutEmpty.setVisibility(View.VISIBLE);
                    }


                    progressDialog.dismiss();

                    carsAdapter = new salesCarsAdapter(getActivity(), carList);
                    recyclerView.setAdapter(carsAdapter);

                    Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
