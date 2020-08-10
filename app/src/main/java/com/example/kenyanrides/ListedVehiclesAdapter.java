package com.example.kenyanrides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListedVehiclesAdapter extends RecyclerView.Adapter<ListedVehiclesAdapter.MyViewHolder> {

    private Context mContext;
    private List<ListVehicle> ListedVehiclesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listedVehicleTitle, listedVehiclePrice;
        public ImageView listedVehicleImage;
        public Button btnListedVehicleEdit;

        public MyViewHolder(View view) {
            super(view);
            listedVehicleTitle = (TextView) view.findViewById(R.id.textViewCarName);
            listedVehiclePrice = (TextView) view.findViewById(R.id.textViewPrice);
            listedVehicleImage = (ImageView) view.findViewById(R.id.listedVehicleImageView);
            btnListedVehicleEdit = view.findViewById(R.id.btnListedVehicleEdit);
        }
    }

    public ListedVehiclesAdapter(Context mContext, List<ListVehicle> ListedVehiclesList) {
        this.mContext = mContext;
        this.ListedVehiclesList = ListedVehiclesList;
    }


    @NonNull
    @Override
    public ListedVehiclesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listed_vehicles_card_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ListedVehiclesAdapter.MyViewHolder holder, int position) {

        ListVehicle listVehicle = ListedVehiclesList.get(position);
        holder.listedVehicleTitle.setText(listVehicle.getListedVehicleName());
        holder.listedVehiclePrice.setText("Ksh " + listVehicle.getListedVehiclePrice() + "/day" );

        // loading album cover using Glide library
        Glide.with(mContext).load(listVehicle.getListedVehicleImage()).into(holder.listedVehicleImage);

        holder.btnListedVehicleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open editing activity
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListedVehiclesList.size();
    }
}
