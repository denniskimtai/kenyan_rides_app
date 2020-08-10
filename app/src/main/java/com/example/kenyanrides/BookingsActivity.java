package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingsAdapter adapter;
    private List<VehicleBookings> vehicleBookingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        recyclerView = (RecyclerView) findViewById(R.id.bookingsRecyclerView);

        vehicleBookingsList = new ArrayList<>();
        adapter = new BookingsAdapter(this, vehicleBookingsList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        prepareBookedCars();


    }

    private void prepareBookedCars() {

        int[] covers = new int[]{
                R.drawable.car,
                R.drawable.mazda,
                R.drawable.vitz
        };

        VehicleBookings a = new VehicleBookings("Mercedes Benz", "9/8/2020","12/8/2020","Eldoret",2000, covers[0]);
        vehicleBookingsList.add(a);

        a = new VehicleBookings("Mazda Cx5", "9/8/2020","12/8/2020","Kitale",1000, covers[1]);
        vehicleBookingsList.add(a);

        a = new VehicleBookings("Toyota Vitz", "9/8/2020","12/8/2020","Nairobi",800, covers[2]);
        vehicleBookingsList.add(a);

    }
}