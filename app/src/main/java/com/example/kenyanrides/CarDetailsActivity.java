package com.example.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CarDetailsActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static String[] IMAGES ;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    private TextView txt_vehicle_title, txt_vehicle_overview, txt_price_per_day, txt_powered_by, txt_location, txt_model_year, txt_seating_capacity, txt_driver_status, txt_owner_id;

    private ImageView imgAirConditioner, imgAntilockBrakingSystem, imgPowerSteering, imgPowerWindows, imgCdPlayer, imgLeatherSeats, imgCentralLocking,
    imgPowerDoorLocks, imgBrakeAssist, imgDriverAirbag, imgPassengerAirbag, imgCrashSensor;

    private Button btnBookNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        //initialize views
        txt_vehicle_title = findViewById(R.id.txt_vehicle_title);
        txt_vehicle_overview = findViewById(R.id.txt_vehicle_overview);
        txt_price_per_day = findViewById(R.id.txt_vehicle_price);
        txt_powered_by = findViewById(R.id.txt_vehicle_powered_by);
        txt_location = findViewById(R.id.txt_vehicle_location);
        txt_model_year = findViewById(R.id.txt_vehicle_model_year);
        txt_seating_capacity = findViewById(R.id.txt_vehicle_seats);
        txt_driver_status = findViewById(R.id.txt_vehicle_driver_status);
        txt_owner_id = findViewById(R.id.txt_vehicle_owner_id);

        //initializing image views
        imgAirConditioner = findViewById(R.id.img_air_conditioner);
        imgAntilockBrakingSystem = findViewById(R.id.img_antilock_braking_system);
        imgPowerSteering = findViewById(R.id.img_power_steering);
        imgPowerWindows = findViewById(R.id.img_power_windows);
        imgCdPlayer = findViewById(R.id.img_cd_player);
        imgLeatherSeats = findViewById(R.id.img_leather_seats);
        imgCentralLocking = findViewById(R.id.img_central_locking);
        imgPowerDoorLocks = findViewById(R.id.img_power_door_locks);
        imgBrakeAssist = findViewById(R.id.img_brake_assist);
        imgDriverAirbag = findViewById(R.id.img_driver_airbags);
        imgPassengerAirbag = findViewById(R.id.img_passenger_airbag);
        imgCrashSensor = findViewById(R.id.img_crash_sensor);

        //get text from intent
        String vehicle_title = getIntent().getStringExtra("vehicle_title");
        String vehicle_overview = getIntent().getStringExtra("vehicle_overview");
        int price_per_day = getIntent().getIntExtra("price_per_day", 0);
        String powered_by = getIntent().getStringExtra("powered_by");
        String location = getIntent().getStringExtra("location");
        String model_year = getIntent().getStringExtra("model_year");
        String seating_capacity = getIntent().getStringExtra("seating_capacity");
        String driver_status = getIntent().getStringExtra("driver_status");
        String owner_id = getIntent().getStringExtra("owner_id");

        String airConditioner = getIntent().getStringExtra("airConditioner");
        String powerDoorLocks = getIntent().getStringExtra("powerDoorLocks");
        String antiLockBrakingSystem = getIntent().getStringExtra("antiLockBrakingSystem");
        String brakeAssist = getIntent().getStringExtra("brakeAssist");
        String powerSteering = getIntent().getStringExtra("powerSteering");
        String driverAirbag = getIntent().getStringExtra("driverAirbag");
        String passengerAirbag = getIntent().getStringExtra("passengerAirbag");
        String powerWindows = getIntent().getStringExtra("powerWindows");
        String cdPlayer = getIntent().getStringExtra("cdPlayer");
        String centralLocking = getIntent().getStringExtra("centralLocking");
        String crashSensor = getIntent().getStringExtra("crashSensor");
        String leatherSeats = getIntent().getStringExtra("leatherSeats");


        //set text to their fields
        txt_vehicle_title.setText(vehicle_title);
        txt_vehicle_overview.setText(vehicle_overview);
        txt_price_per_day.setText("Ksh " + price_per_day);
        txt_powered_by.setText(powered_by);
        txt_location.setText(location);
        txt_model_year.setText(model_year);
        txt_seating_capacity.setText(seating_capacity);
        txt_driver_status.setText(driver_status);
        txt_owner_id.setText(owner_id);

        //setimage resource
        if (airConditioner == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (antiLockBrakingSystem == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerSteering == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerWindows == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (cdPlayer == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (leatherSeats == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (centralLocking == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerDoorLocks == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (brakeAssist == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (driverAirbag == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (passengerAirbag == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (crashSensor == "1") {
            imgAirConditioner.setImageResource(R.drawable.icon_checked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageResource(R.drawable.icon_unchecked_box);
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }




        //get images from intent
        String image1 = getIntent().getStringExtra("image1");
        String image2 = getIntent().getStringExtra("image2");
        String image3 = getIntent().getStringExtra("image3");
        String image4 = getIntent().getStringExtra("image4");
        String image5 = getIntent().getStringExtra("image5");

        IMAGES = new String[]{image1, image2, image3, image4, image5};


        init();

        btnBookNow = findViewById(R.id.btnBookNow);

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(CarDetailsActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.activity_book_now);
                dialog.show();


            }
        });



    }
//
    private void init(){



        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new ViewpagerAdapter(ImagesArray,CarDetailsActivity.this));



        final float density = getResources().getDisplayMetrics().density;


        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);



    }
//

}






