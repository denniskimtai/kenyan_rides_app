package com.example.kenyanrides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.MyViewHolder> {

    public Context mContext;
    private List<car> carsList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView carPrice, carName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            carName = (TextView) view.findViewById(R.id.carName);
            carPrice = (TextView) view.findViewById(R.id.carPrice);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }


    }


    public CarsAdapter(Context mContext, List<car> carsList) {
        this.mContext = mContext;
        this.carsList = carsList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final car car = carsList.get(position);
        holder.carName.setText(car.getCarName());
        holder.carPrice.setText("Ksh: " + car.getCarPrice() + "/day");

        Glide.with(mContext).load(car.getImage()).into(holder.thumbnail);


        //on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open details activity

                Intent intent = new Intent(view.getContext(), CarDetailsActivity.class);

                //send car details to cardetails activity

                intent.putExtra("vehicle_title", car.getCarName());
                intent.putExtra("vehicle_brand", car.getVehicleBrand());
                intent.putExtra("vehicle_overview", car.getVehicleOverview());
                intent.putExtra("price_per_day", car.getCarPrice());
                intent.putExtra("powered_by", car.getPoweredBy());
                intent.putExtra("location", car.getLocation());
                intent.putExtra("model_year", car.getModelYear());
                intent.putExtra("seating_capacity", car.getSeatingCapacity());
                intent.putExtra("driver_status", car.getDriverStatus());
                intent.putExtra("image1", car.getImage());
                intent.putExtra("image2", car.getImage2());
                intent.putExtra("image3", car.getImage3());
                intent.putExtra("image4", car.getImage4());
                intent.putExtra("image5", car.getImage5());
                intent.putExtra("owner_id", car.getOwnerId());
                intent.putExtra("reg_date", car.getRegDate());

                //vehicle accessories
                intent.putExtra("airConditioner", car.getAirConditioner());
                intent.putExtra("powerDoorLocks", car.getPowerDoorLocks());
                intent.putExtra("antiLockBrakingSystem", car.getAntiLockBrakingSystem());
                intent.putExtra("brakeAssist", car.getBrakeAssist());
                intent.putExtra("powerSteering", car.getPowerSteering());
                intent.putExtra("driverAirbag", car.getDriverAirbag());
                intent.putExtra("passengerAirbag", car.getPassengerAirbag());
                intent.putExtra("powerWindows", car.getPowerWindows());
                intent.putExtra("cdPlayer", car.getCdPlayer());
                intent.putExtra("centralLocking", car.getCentralLocking());
                intent.putExtra("crashSensor", car.getCrashSensor());
                intent.putExtra("leatherSeats", car.getLeatherSeats());


                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }
}
