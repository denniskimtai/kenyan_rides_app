package com.example.kenyanrides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<VehicleBookings> vehicleBookingsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vehicleName, fromDate, toDate, travelDestination, priceDue;
        public ImageView vehicleImage;

        public MyViewHolder(View view) {
            super(view);
            vehicleName = view.findViewById(R.id.textViewCarName);
            fromDate = view.findViewById(R.id.textViewFromDate);
            toDate = view.findViewById(R.id.textViewToDate);
            travelDestination = view.findViewById(R.id.textViewTravelDestination);
            vehicleImage = (ImageView) view.findViewById(R.id.bookingsImageView);
            priceDue = view.findViewById(R.id.textViewPriceDue);
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
        holder.vehicleName.setText(vehicleBookings.getVehicleName());
        holder.fromDate.setText(vehicleBookings.getFromDate());
        holder.toDate.setText(vehicleBookings.getToDate());
        holder.travelDestination.setText(vehicleBookings.getTravelDestination());
        holder.priceDue.setText("Ksh " + vehicleBookings.getPriceDue() + "/day");

        // loading album cover using Glide library
        Glide.with(mContext).load(vehicleBookings.getVehicleImage()).into(holder.vehicleImage);

//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return vehicleBookingsList.size();
    }
}
