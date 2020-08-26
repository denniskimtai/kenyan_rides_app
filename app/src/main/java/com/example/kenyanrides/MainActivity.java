package com.example.kenyanrides;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the default fragment
        loadFragment(new TablayoutFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_nav_view);



        navigation.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);



    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_explore:
                fragment = new TablayoutFragment();
                break;

            case R.id.navigation_sell:

                //if the user is not logged in
                //starting the login activity
                if (!SharedPrefManager.getInstance(this).isLoggedIn()) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else {

                    showAlertDialog();
                }

                break;

            case R.id.navigation_motors:
                //if the user is not logged in
                //starting the login activity
                if (!SharedPrefManager.getInstance(this).isLoggedIn()) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else {
                    fragment = new AccountFragment();
                }

                break;



        }

        return loadFragment(fragment);

    }

    private void showAlertDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sale_rental_dialog_layout);
        dialog.show();

        //initialize dialog views
        ImageView forSale = dialog.findViewById(R.id.image_view_for_sale);
        ImageView forRent = dialog.findViewById(R.id.image_view_for_rent);



        //onclicklisteners
        forSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Fragment fragment2;
                fragment2 = new SellSalesFragment();
                loadFragment(fragment2);

            }
        });

        forRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Fragment fragment2;
                fragment2 = new SellFragment();
                loadFragment(fragment2);


            }
        });




    }


}