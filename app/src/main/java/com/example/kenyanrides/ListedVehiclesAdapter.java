package com.example.kenyanrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
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

public class ListedVehiclesAdapter extends RecyclerView.Adapter<ListedVehiclesAdapter.MyViewHolder> {

    private Context mContext;
    private List<ListVehicle> ListedVehiclesList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    String delete_vehicle_url = "https://kenyanrides.com/android/delete_vehicle.php";
    private final String updateVehicleStatusUrl = "https://kenyanrides.com/android/update_vehicle_status.php";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listedVehicleTitle, listedVehiclePrice, listedVehicleStatus, textViewComplete, textViewDelete, textViewLocation;
        public ImageView listedVehicleImage;



        public MyViewHolder(View view) {
            super(view);
            listedVehicleTitle = view.findViewById(R.id.textViewCarName);
            listedVehiclePrice = view.findViewById(R.id.textViewPrice);
            listedVehicleImage = view.findViewById(R.id.listedVehicleImageView);
            listedVehicleStatus = view.findViewById(R.id.textViewStatus);
            textViewComplete = view.findViewById(R.id.textViewComplete);
            textViewDelete = view.findViewById(R.id.btnListedVehicleDelete);
            textViewLocation = view.findViewById(R.id.textViewLocation);

            alertDialogBuilder = new AlertDialog.Builder(mContext);
            progressDialog = new ProgressDialog(mContext);
        }
    }

    public ListedVehiclesAdapter(Context mContext, List<ListVehicle> ListedVehiclesList) {
        this.mContext = mContext;
        this.ListedVehiclesList = ListedVehiclesList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listed_vehicles_card_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ListedVehiclesAdapter.MyViewHolder holder, int position) {

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
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewDelete.setVisibility(View.VISIBLE);

                holder.textViewComplete.setText("Edit Vehicle");
                //go to edit vehicle
                holder.textViewComplete.setOnClickListener(view -> {

                    //go to edit activity
                    Intent intent = new Intent(view.getContext(), EditVehicleActivity.class);

                    //send car details to EditVehicleActivity

                    intent.putExtra("vehicle_id", listVehicle.getId());
                    intent.putExtra("vehicle_title", listVehicle.getCarName());
                    intent.putExtra("vehicle_brand", listVehicle.getVehicleBrand());
                    intent.putExtra("vehicle_overview", listVehicle.getVehicleOverview());
                    intent.putExtra("price_per_day", listVehicle.getCarPrice());
                    intent.putExtra("powered_by", listVehicle.getPoweredBy());
                    intent.putExtra("location", listVehicle.getLocation());
                    intent.putExtra("model_year", listVehicle.getModelYear());
                    intent.putExtra("seating_capacity", listVehicle.getSeatingCapacity());
                    intent.putExtra("driver_status", listVehicle.getDriverStatus());
                    intent.putExtra("image1", listVehicle.getImage());
                    intent.putExtra("image2", listVehicle.getImage2());
                    intent.putExtra("image3", listVehicle.getImage3());
                    intent.putExtra("image4", listVehicle.getImage4());
                    intent.putExtra("image5", listVehicle.getImage5());
                    intent.putExtra("owner_id", listVehicle.getOwnerId());
                    intent.putExtra("reg_date", listVehicle.getRegDate());
                    intent.putExtra("vehicle_status", listVehicle.getBooked());

                    //vehicle accessories
                    intent.putExtra("airConditioner", listVehicle.getAirConditioner());
                    intent.putExtra("powerDoorLocks", listVehicle.getPowerDoorLocks());
                    intent.putExtra("antiLockBrakingSystem", listVehicle.getAntiLockBrakingSystem());
                    intent.putExtra("brakeAssist", listVehicle.getBrakeAssist());
                    intent.putExtra("powerSteering", listVehicle.getPowerSteering());
                    intent.putExtra("driverAirbag", listVehicle.getDriverAirbag());
                    intent.putExtra("passengerAirbag", listVehicle.getPassengerAirbag());
                    intent.putExtra("powerWindows", listVehicle.getPowerWindows());
                    intent.putExtra("cdPlayer", listVehicle.getCdPlayer());
                    intent.putExtra("centralLocking", listVehicle.getCentralLocking());
                    intent.putExtra("crashSensor", listVehicle.getCrashSensor());
                    intent.putExtra("leatherSeats", listVehicle.getLeatherSeats());

                    ((Activity)mContext).finish();
                    view.getContext().startActivity(intent);


                });

                //delete vehicle
                holder.textViewDelete.setOnClickListener(view -> {

                    alertDialogBuilder.setTitle("Warning!");
                    alertDialogBuilder.setMessage("You are about to delete your vehicle listing from the system!\n\nThis is process cannot be reversed are you sure you want to delete?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {

                        //delete vehicle

                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                delete_vehicle_url,
                                response -> {

                                    //fetch results from api
                                    switch (response) {

                                        case "Vehicle deleted successfully":
                                            alertDialogBuilder.setMessage("Vehicle deleted");
                                            alertDialogBuilder.setCancelable(false);
                                            alertDialogBuilder.setPositiveButton("Ok", (dialogInterface1, i1) -> {

                                                //go to main activity
                                                Intent intent = new Intent(mContext, MainActivity.class);
                                                mContext.startActivity(intent);
                                                ((Activity) mContext).finish();

                                            });
                                            alertDialogBuilder.show();

                                            break;

                                        case "Vehicle failed to delete":
                                            alertDialogBuilder.setTitle("Failed!");
                                            alertDialogBuilder.setMessage("Vehicle not deleted! Please try again");
                                            alertDialogBuilder.setCancelable(false);
                                            alertDialogBuilder.setPositiveButton("Try again", (dialogInterface12, i12) -> {

                                            });
                                            alertDialogBuilder.show();

                                            break;

                                    }
                                    progressDialog.dismiss();


                                }, error -> {

                            progressDialog.dismiss();

                            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }){
                            //send params needed to db
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("vehicleId", String.valueOf(listVehicle.getId()));


                                return params;

                            }
                        };

                        Volley.newRequestQueue(mContext).add(stringRequest);


                    });

                    alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                        //cancel alert dialog

                    });

                    alertDialogBuilder.show();

                });


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
                holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Error! You can't delete a car that is booked!", Toast.LENGTH_LONG).show();
                    }
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

                holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Error! You can't delete a car that is booked!", Toast.LENGTH_LONG).show();
                    }
                });


                break;

            case "7":
                holder.listedVehicleStatus.setText("Returned");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewComplete.setText("Make Vehicle Available");
                holder.textViewComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //update status of vehicle to 5
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
                //delete vehicle from db
                holder.textViewDelete.setOnClickListener(view -> {

                    alertDialogBuilder.setTitle("Warning!");
                    alertDialogBuilder.setMessage("You are about to delete " + listVehicle.getCarName() + " from the system!\n This process cannot be undone");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //delete vehicle

                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    delete_vehicle_url,
                                    response -> {

                                        //fetch results from api
                                        switch (response) {

                                            case "Vehicle deleted successfully":
                                                alertDialogBuilder.setMessage("Vehicle deleted");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Ok", (dialogInterface1, i1) -> {

                                                    //go to main activity
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();

                                                });
                                                alertDialogBuilder.show();

                                                break;

                                            case "Vehicle failed to delete":
                                                alertDialogBuilder.setTitle("Failed!");
                                                alertDialogBuilder.setMessage("Vehicle not deleted! Please try again");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Try again", (dialogInterface12, i12) -> {

                                                });
                                                alertDialogBuilder.show();

                                                break;
                                        }

                                        progressDialog.dismiss();


                                    }, error -> {

                                progressDialog.dismiss();

                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }){
                                //send params needed to db
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("vehicleId", String.valueOf(listVehicle.getId()));


                                    return params;

                                }
                            };

                            Volley.newRequestQueue(mContext).add(stringRequest);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialogBuilder.show();


                });

                break;

            case "9":
                holder.listedVehicleStatus.setText("Offline");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewDelete.setVisibility(View.VISIBLE);

                holder.textViewComplete.setText("Edit Vehicle");
                holder.textViewComplete.setOnClickListener(view -> {

                    //go to edit activity
                    Intent intent = new Intent(view.getContext(), EditVehicleActivity.class);

                    //send car details to EditVehicleActivity

                    intent.putExtra("vehicle_id", listVehicle.getId());
                    intent.putExtra("vehicle_title", listVehicle.getCarName());
                    intent.putExtra("vehicle_brand", listVehicle.getVehicleBrand());
                    intent.putExtra("vehicle_overview", listVehicle.getVehicleOverview());
                    intent.putExtra("price_per_day", listVehicle.getCarPrice());
                    intent.putExtra("powered_by", listVehicle.getPoweredBy());
                    intent.putExtra("location", listVehicle.getLocation());
                    intent.putExtra("model_year", listVehicle.getModelYear());
                    intent.putExtra("seating_capacity", listVehicle.getSeatingCapacity());
                    intent.putExtra("driver_status", listVehicle.getDriverStatus());
                    intent.putExtra("image1", listVehicle.getImage());
                    intent.putExtra("image2", listVehicle.getImage2());
                    intent.putExtra("image3", listVehicle.getImage3());
                    intent.putExtra("image4", listVehicle.getImage4());
                    intent.putExtra("image5", listVehicle.getImage5());
                    intent.putExtra("owner_id", listVehicle.getOwnerId());
                    intent.putExtra("reg_date", listVehicle.getRegDate());
                    intent.putExtra("vehicle_status", listVehicle.getBooked());

                    //vehicle accessories
                    intent.putExtra("airConditioner", listVehicle.getAirConditioner());
                    intent.putExtra("powerDoorLocks", listVehicle.getPowerDoorLocks());
                    intent.putExtra("antiLockBrakingSystem", listVehicle.getAntiLockBrakingSystem());
                    intent.putExtra("brakeAssist", listVehicle.getBrakeAssist());
                    intent.putExtra("powerSteering", listVehicle.getPowerSteering());
                    intent.putExtra("driverAirbag", listVehicle.getDriverAirbag());
                    intent.putExtra("passengerAirbag", listVehicle.getPassengerAirbag());
                    intent.putExtra("powerWindows", listVehicle.getPowerWindows());
                    intent.putExtra("cdPlayer", listVehicle.getCdPlayer());
                    intent.putExtra("centralLocking", listVehicle.getCentralLocking());
                    intent.putExtra("crashSensor", listVehicle.getCrashSensor());
                    intent.putExtra("leatherSeats", listVehicle.getLeatherSeats());

                    ((Activity)mContext).finish();
                    view.getContext().startActivity(intent);


                });

                //delete vehicle from db
                holder.textViewDelete.setOnClickListener(view -> {

                    alertDialogBuilder.setTitle("Warning!");
                    alertDialogBuilder.setMessage("You are about to delete " + listVehicle.getCarName() + " from the system!\n This process cannot be undone");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //delete vehicle

                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    delete_vehicle_url,
                                    response -> {

                                        //fetch results from api
                                        switch (response) {

                                            case "Vehicle deleted successfully":
                                                alertDialogBuilder.setMessage("Vehicle deleted");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Ok", (dialogInterface1, i1) -> {

                                                    //go to main activity
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();

                                                });
                                                alertDialogBuilder.show();

                                                break;

                                            case "Vehicle failed to delete":
                                                alertDialogBuilder.setTitle("Failed!");
                                                alertDialogBuilder.setMessage("Vehicle not deleted! Please try again");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Try again", (dialogInterface12, i12) -> {

                                                });
                                                alertDialogBuilder.show();

                                                break;
                                        }

                                        progressDialog.dismiss();


                                    }, error -> {

                                progressDialog.dismiss();

                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }){
                                //send params needed to db
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("vehicleId", String.valueOf(listVehicle.getId()));


                                    return params;

                                }
                            };

                            Volley.newRequestQueue(mContext).add(stringRequest);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialogBuilder.show();


                });
                break;

            case "10":

                holder.listedVehicleStatus.setText("For Sale");

                //if car listed is for sale
                holder.textViewComplete.setText("Edit Vehicle");
                //go to edit vehicle
                holder.textViewComplete.setOnClickListener(view -> {

                    //go to edit activity
                    Intent intent = new Intent(view.getContext(), EditVehicleActivity.class);

                    //send car details to EditVehicleActivity

                    intent.putExtra("vehicle_id", listVehicle.getId());
                    intent.putExtra("vehicle_title", listVehicle.getCarName());
                    intent.putExtra("vehicle_brand", listVehicle.getVehicleBrand());
                    intent.putExtra("vehicle_overview", listVehicle.getVehicleOverview());
                    intent.putExtra("price_per_day", listVehicle.getCarPrice());
                    intent.putExtra("powered_by", listVehicle.getPoweredBy());
                    intent.putExtra("location", listVehicle.getLocation());
                    intent.putExtra("model_year", listVehicle.getModelYear());
                    intent.putExtra("seating_capacity", listVehicle.getSeatingCapacity());
                    intent.putExtra("driver_status", listVehicle.getDriverStatus());
                    intent.putExtra("image1", listVehicle.getImage());
                    intent.putExtra("image2", listVehicle.getImage2());
                    intent.putExtra("image3", listVehicle.getImage3());
                    intent.putExtra("image4", listVehicle.getImage4());
                    intent.putExtra("image5", listVehicle.getImage5());
                    intent.putExtra("owner_id", listVehicle.getOwnerId());
                    intent.putExtra("reg_date", listVehicle.getRegDate());
                    intent.putExtra("vehicle_status", listVehicle.getBooked());

                    //vehicle accessories
                    intent.putExtra("airConditioner", listVehicle.getAirConditioner());
                    intent.putExtra("powerDoorLocks", listVehicle.getPowerDoorLocks());
                    intent.putExtra("antiLockBrakingSystem", listVehicle.getAntiLockBrakingSystem());
                    intent.putExtra("brakeAssist", listVehicle.getBrakeAssist());
                    intent.putExtra("powerSteering", listVehicle.getPowerSteering());
                    intent.putExtra("driverAirbag", listVehicle.getDriverAirbag());
                    intent.putExtra("passengerAirbag", listVehicle.getPassengerAirbag());
                    intent.putExtra("powerWindows", listVehicle.getPowerWindows());
                    intent.putExtra("cdPlayer", listVehicle.getCdPlayer());
                    intent.putExtra("centralLocking", listVehicle.getCentralLocking());
                    intent.putExtra("crashSensor", listVehicle.getCrashSensor());
                    intent.putExtra("leatherSeats", listVehicle.getLeatherSeats());

                    ((Activity)mContext).finish();
                    view.getContext().startActivity(intent);


                });

                //delete vehicle from db
                holder.textViewDelete.setOnClickListener(view -> {

                    alertDialogBuilder.setTitle("Warning!");
                    alertDialogBuilder.setMessage("You are about to delete " + listVehicle.getCarName() + " from the system!\n This process cannot be undone");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //delete vehicle

                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    delete_vehicle_url,
                                    response -> {

                                        //fetch results from api
                                        switch (response) {

                                            case "Vehicle deleted successfully":
                                                alertDialogBuilder.setMessage("Vehicle deleted");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Ok", (dialogInterface1, i1) -> {

                                                    //go to main activity
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();

                                                });
                                                alertDialogBuilder.show();

                                                break;

                                            case "Vehicle failed to delete":
                                                alertDialogBuilder.setTitle("Failed!");
                                                alertDialogBuilder.setMessage("Vehicle not deleted! Please try again");
                                                alertDialogBuilder.setCancelable(false);
                                                alertDialogBuilder.setPositiveButton("Try again", (dialogInterface12, i12) -> {

                                                });
                                                alertDialogBuilder.show();

                                                break;
                                        }

                                        progressDialog.dismiss();


                                    }, error -> {

                                progressDialog.dismiss();

                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }){
                                //send params needed to db
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("vehicleId", String.valueOf(listVehicle.getId()));


                                    return params;

                                }
                            };

                            Volley.newRequestQueue(mContext).add(stringRequest);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialogBuilder.show();


                });


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
