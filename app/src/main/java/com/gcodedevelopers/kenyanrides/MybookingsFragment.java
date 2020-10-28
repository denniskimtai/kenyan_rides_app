package com.gcodedevelopers.kenyanrides;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout linearLayoutEmpty;
    private BookingsAdapter adapter;
    private List<VehicleBookings> vehicleBookingsList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private final String bookingListUrl = "https://kenyanrides.com/android/user_bookings.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_mybookings, null);

        recyclerView = myView.findViewById(R.id.bookingsRecyclerView);
        linearLayoutEmpty = myView.findViewById(R.id.linearEmpty);

        vehicleBookingsList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);


        prepareBookedCars();

        return myView;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void prepareBookedCars() {

        //check if network is connected
        if (!isNetworkAvailable()){
            progressDialog.dismiss();

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    prepareBookedCars();

                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
            return;
        }

        progressDialog = new ProgressDialog(getActivity());

        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //getting the current user
        user user = SharedPrefManager.getInstance(getActivity()).getUser();

        String user_email = user.getEmail();

        //fetch from database
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                bookingListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (!(response.equals("No records matching your query were found."))) {

                            //fetch json object returned by api
                            try {
                                JSONArray bookingsJson = new JSONArray(response);

                                recyclerView.setVisibility(View.VISIBLE);
                                linearLayoutEmpty.setVisibility(View.GONE);

                                for (int i = 0; i < bookingsJson.length(); i++) {

                                    //get json objects
                                    JSONObject bookingsObject = bookingsJson.getJSONObject(i);

                                    int vehicleId = bookingsObject.getInt("VehicleId");
                                    String vehicleTitle = bookingsObject.getString("VehiclesTitle");

                                    String Status = bookingsObject.getString("Status");
                                    String FromDate = bookingsObject.getString("FromDate");
                                    String ToDate = bookingsObject.getString("ToDate");
                                    int totalpricedue = bookingsObject.getInt("totalpricedue");
                                    String TravelDestination = bookingsObject.getString("TravelDestination");
                                    String Vimage1 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + bookingsObject.getString("Vimage1");
                                    String image1Editted = Vimage1.replace(" ", "%20");
                                    String BrandName = bookingsObject.getString("BrandName");

                                    VehicleBookings vehicleBookings = new VehicleBookings(vehicleTitle, FromDate, ToDate, TravelDestination,
                                            totalpricedue, image1Editted, Status, bookingsJson.length(), vehicleId, BrandName);

                                    vehicleBookingsList.add(vehicleBookings);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {

                            recyclerView.setVisibility(View.GONE);
                            linearLayoutEmpty.setVisibility(View.VISIBLE);

                        }

                        progressDialog.dismiss();

                        adapter = new BookingsAdapter(getActivity(), vehicleBookingsList);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

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

        Volley.newRequestQueue(getActivity()).add(stringRequest);



    }
}
