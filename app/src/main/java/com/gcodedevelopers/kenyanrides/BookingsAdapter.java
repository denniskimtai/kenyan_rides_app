package com.gcodedevelopers.kenyanrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<VehicleBookings> vehicleBookingsList;

    private final String updateVehicleStatusUrl = "https://kenyanrides.com/android/update_vehicle_status.php";

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vehicleName, fromDate, toDate, travelDestination, priceDue, vehicleStatus, textViewDelivered;
        public ImageView vehicleImage;
        public MyViewHolder(View view) {
            super(view);
            vehicleName = view.findViewById(R.id.textViewCarName);
            fromDate = view.findViewById(R.id.textViewFromDate);
            toDate = view.findViewById(R.id.textViewToDate);
            travelDestination = view.findViewById(R.id.textViewTravelDestination);
            vehicleImage = view.findViewById(R.id.bookingsImageView);
            priceDue = view.findViewById(R.id.textViewPriceDue);
            vehicleStatus = view.findViewById(R.id.textViewStatus);
            textViewDelivered = view.findViewById(R.id.textViewDelivered);

            alertDialogBuilder = new AlertDialog.Builder(mContext);
            progressDialog = new ProgressDialog(mContext);
        }
    }

    public BookingsAdapter(Context mContext, List<VehicleBookings> vehicleBookingsList) {
        this.mContext = mContext;
        this.vehicleBookingsList = vehicleBookingsList;
    }


    @NonNull
    @Override
    public BookingsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_booked_card_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.MyViewHolder holder, int position) {

        VehicleBookings vehicleBookings = vehicleBookingsList.get(position);
        holder.vehicleName.setText(vehicleBookings.getBrandName() + " " + vehicleBookings.getVehicleName());
        holder.fromDate.setText(vehicleBookings.getFromDate());
        holder.toDate.setText(vehicleBookings.getToDate());
        holder.travelDestination.setText(vehicleBookings.getTravelDestination());
        holder.priceDue.setText("Ksh " + vehicleBookings.getPriceDue() + "/day");

        //check status of vehicle
        switch (vehicleBookings.getVehicleStatus()){
            case "1":
                holder.vehicleStatus.setText("Available");
                holder.textViewDelivered.setVisibility(View.GONE);
                break;

            case "3":
                holder.vehicleStatus.setText("In Progress");
                break;

            case "5":
                holder.vehicleStatus.setText("Delivered");
                holder.textViewDelivered.setVisibility(View.GONE);
                break;

            case "7":
                holder.vehicleStatus.setText("Completed");
                holder.textViewDelivered.setVisibility(View.GONE);
                break;

            case "9":
                holder.vehicleStatus.setText("Offline");
                holder.textViewDelivered.setVisibility(View.GONE);
                break;
        }


        // loading album cover using Glide library
        Glide.with(mContext).load(vehicleBookings.getVehicleImage()).into(holder.vehicleImage);

        holder.textViewDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update status of vehicle to 5
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //getting the current user
                user user = SharedPrefManager.getInstance(mContext).getUser();

                String user_email = user.getEmail();

                //fetch from database
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        updateVehicleStatusUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Update Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                ((Activity)mContext).finish();
                                view.getContext().startActivity(intent);



                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                ){
                    //send params needed to db
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("VehicleId", String.valueOf(vehicleBookings.getId()));
                        params.put("user_email", user_email);
                        params.put("status", "5");

                        return params;

                    }
                };

                Volley.newRequestQueue(mContext).add(stringRequest);

            }
        });


    }

    @Override
    public int getItemCount() {
        return vehicleBookingsList.size();
    }
}
