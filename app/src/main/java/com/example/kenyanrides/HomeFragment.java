package com.example.kenyanrides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarsAdapter carsAdapter;
    private List<car> carList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, null);

        recyclerView = myView.findViewById(R.id.cars_recycler_view);


        carList = new ArrayList<>();

        carsAdapter = new CarsAdapter(getActivity(), carList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(carsAdapter);

        prepareCars();



        return myView;


    }

    private void prepareCars() {

        int[] carsImages = new int[]{
                R.drawable.mazda,
                R.drawable.bmw,
                R.drawable.allion,
                R.drawable.vitz,

                R.drawable.mazda,
                R.drawable.bmw,
                R.drawable.allion,
                R.drawable.vitz



        };

        car a = new car(carsImages[0], "Mazda CX5","2000");
        carList.add(a);

        a = new car(carsImages[1], "BMW","5000");
        carList.add(a);

        a = new car(carsImages[2], "Toyota Allion","1000");
        carList.add(a);

        a = new car(carsImages[3], "Toyota Vitz","800");
        carList.add(a);

        a = new car(carsImages[4], "Mazda CX5","1000");
        carList.add(a);

        a = new car(carsImages[5], "BMW","4000");
        carList.add(a);

        a = new car(carsImages[6], "Toyota Allion","1200");
        carList.add(a);

        a = new car(carsImages[7], "Toyota Vitz","100");
        carList.add(a);

        carsAdapter.notifyDataSetChanged();

    }
}
