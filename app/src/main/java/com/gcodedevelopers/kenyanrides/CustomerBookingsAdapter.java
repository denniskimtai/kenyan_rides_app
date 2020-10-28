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
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerBookingsAdapter extends RecyclerView.Adapter<CustomerBookingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ListVehicle> ListedVehiclesList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    String delete_vehicle_url = "https://kenyanrides.com/android/delete_vehicle.php";
    private final String updateVehicleStatusUrl = "https://kenyanrides.com/android/update_vehicle_status.php";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listedVehicleTitle, listedVehiclePrice, listedVehicleStatus, textViewComplete, textViewLocation;
        public ImageView listedVehicleImage;
        public LinearLayout linear_layout_contact;



        public MyViewHolder(View view) {
            super(view);
            listedVehicleTitle = view.findViewById(R.id.textViewCarName);
            listedVehiclePrice = view.findViewById(R.id.textViewPrice);
            listedVehicleImage = view.findViewById(R.id.listedVehicleImageView);
            listedVehicleStatus = view.findViewById(R.id.textViewStatus);
            textViewComplete = view.findViewById(R.id.textViewComplete);

            textViewLocation = view.findViewById(R.id.textViewLocation);
            linear_layout_contact = view.findViewById(R.id.linear_layout_contact);

            alertDialogBuilder = new AlertDialog.Builder(mContext);
            progressDialog = new ProgressDialog(mContext);
        }
    }

    public CustomerBookingsAdapter(Context mContext, List<ListVehicle> ListedVehiclesList) {
        this.mContext = mContext;
        this.ListedVehiclesList = ListedVehiclesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_bookings_card_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerBookingsAdapter.MyViewHolder holder, int position) {

        ListVehicle listVehicle = ListedVehiclesList.get(position);
        holder.listedVehicleTitle.setText(listVehicle.getVehicleBrand() + " " + listVehicle.getCarName());

        //format car price
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(listVehicle.getCarPrice());

        //check if car is for sale and remove /day
        if (listVehicle.getBooked().equals("10")){

            holder.listedVehiclePrice.setText("Ksh " + formatted);

        }else {
            holder.listedVehiclePrice.setText("Ksh " + formatted + "/day");
        }

        holder.textViewLocation.setText(listVehicle.getLocation());


        switch (listVehicle.getBooked()){

            case "1":
                holder.listedVehicleStatus.setText("Available");


                break;

            case "3":

                holder.listedVehicleStatus.setText("Booked");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewComplete.setOnClickListener(view -> {

                   //check if client has marked vehicle as delivered
                alertDialogBuilder.setMessage("Please ask client to mark the vehicle as delivered to continue");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Done", (dialogInterface, i) -> {

                    Intent intent = ((Activity)mContext).getIntent();
                    ((Activity)mContext).finish();
                    view.getContext().startActivity(intent);

                });
                alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {

                });
                alertDialogBuilder.show();


                });

                break;

            case "5":
                holder.listedVehicleStatus.setText("Delivered");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewComplete.setText("Upload Documents");
                holder.textViewComplete.setOnClickListener(view -> {

                    //upload images
                    //go to upload client image activity
                    Intent intent = new Intent(view.getContext(), ClientImageUploadActivity.class);
                    intent.putExtra("vehicle_id", listVehicle.getId());
                    view.getContext().startActivity(intent);



                });




                break;

            case "7":
                holder.listedVehicleStatus.setText("Returned");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewComplete.setText("Make Vehicle Available");
                holder.textViewComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //update status of vehicle to 1
                        progressDialog.setMessage("Loading...");
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
                                params.put("VehicleId", String.valueOf(listVehicle.getId()));
                                params.put("user_email", user_email);
                                params.put("status", "1");

                                return params;

                            }
                        };

                        Volley.newRequestQueue(mContext).add(stringRequest);

                    }
                });


                break;

            case "9":
                holder.listedVehicleStatus.setText("Offline");
                holder.textViewComplete.setVisibility(View.VISIBLE);

                break;

            case "10":
                //dont show cars that are for sale
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                break;


        }

        // loading album cover using Glide library
        Glide.with(mContext).load(listVehicle.getImage()).into(holder.listedVehicleImage);

    }



    @Override
    public int getItemCount() {
        return ListedVehiclesList.size();
    }
}
