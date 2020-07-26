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

        car car = carsList.get(position);
        holder.carName.setText(car.getCarName());
        holder.carPrice.setText("Ksh: " + car.getCarPrice() + "/day");
        holder.thumbnail.setImageResource(car.getImage());


        //on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open details activity

                Intent intent = new Intent(view.getContext(), CarDetailsActivity.class);
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }
}
