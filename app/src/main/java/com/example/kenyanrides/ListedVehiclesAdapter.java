package com.example.kenyanrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListedVehiclesAdapter extends RecyclerView.Adapter<ListedVehiclesAdapter.MyViewHolder> {

    private Context mContext;
    private List<ListVehicle> ListedVehiclesList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listedVehicleTitle, listedVehiclePrice, listedVehicleStatus, textViewComplete;
        public ImageView listedVehicleImage;



        public MyViewHolder(View view) {
            super(view);
            listedVehicleTitle = view.findViewById(R.id.textViewCarName);
            listedVehiclePrice = view.findViewById(R.id.textViewPrice);
            listedVehicleImage = view.findViewById(R.id.listedVehicleImageView);
            listedVehicleStatus = view.findViewById(R.id.textViewStatus);
            textViewComplete = view.findViewById(R.id.textViewComplete);

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
        holder.listedVehicleTitle.setText(listVehicle.getCarName());
        holder.listedVehiclePrice.setText("Ksh " + listVehicle.getCarPrice() + "/day" );


        switch (listVehicle.getBooked()){

            case "1":
                holder.listedVehicleStatus.setText("Available");
                holder.textViewComplete.setVisibility(View.VISIBLE);
                holder.textViewComplete.setText("Edit Vehicle");
                holder.textViewComplete.setBackground(mContext.getResources().getDrawable(R.drawable.buttom_background));
                holder.textViewComplete.setTextColor(mContext.getResources().getColor(R.color.white));
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
                break;

            case "9":
                holder.listedVehicleStatus.setText("Offline");
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
