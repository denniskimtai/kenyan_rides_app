package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalCarImagesAdapter extends RecyclerView.Adapter<HorizontalCarImagesAdapter.MyViewHolder> {

    public Context mContext;
    private ArrayList<Uri> uri;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.horizontalCarImage);

        }


    }


    public HorizontalCarImagesAdapter(Context mContext, ArrayList<Uri> uri) {
        this.mContext = mContext;
        this.uri = uri;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_car_image_item_layout, parent, false);

        return new HorizontalCarImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.thumbnail.setImageURI(uri.get(position));

    }

    @Override
    public int getItemCount() {
        return uri.size();
    }





}
