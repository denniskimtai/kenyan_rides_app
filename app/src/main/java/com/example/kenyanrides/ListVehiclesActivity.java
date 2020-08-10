package com.example.kenyanrides;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ListVehiclesActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ListedVehiclesAdapter adapter;
    private List<ListVehicle> listedVehiclesList;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vehicles);

//        progressDialog = new ProgressDialog(this);
//
//        alertDialogBuilder = new AlertDialog.Builder(this);
//
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.vehiclesListRecyclerView);

        listedVehiclesList = new ArrayList<>();
        adapter = new ListedVehiclesAdapter(this, listedVehiclesList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        
        

        prepareListedVehicles();

        

    }

    private void prepareListedVehicles() {

        int[] covers = new int[]{
                R.drawable.car,
                R.drawable.mazda,
                R.drawable.vitz,
                };

        ListVehicle a = new ListVehicle("Mercedes Benz", 3000, covers[0]);
        listedVehiclesList.add(a);

        a = new ListVehicle("Mazda Cx5", 2000, covers[1]);
        listedVehiclesList.add(a);

        a = new ListVehicle("Toyota Vitz", 1000, covers[2]);
        listedVehiclesList.add(a);


        adapter.notifyDataSetChanged();




    }


}
