package com.example.kenyanrides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CarDetailsActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static String[] IMAGES ;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    private TextView txt_vehicle_title, txt_vehicle_overview, txt_price_per_day, txt_powered_by,
            txt_location, txt_model_year, txt_seating_capacity, txt_driver_status, txt_owner_reg_date, txt_day, text, call;

    private ImageView imgAirConditioner, imgAntilockBrakingSystem, imgPowerSteering, imgPowerWindows, imgCdPlayer, imgLeatherSeats, imgCentralLocking,
    imgPowerDoorLocks, imgBrakeAssist, imgDriverAirbag, imgPassengerAirbag, imgCrashSensor;

    private Button btnBookNow;

    private CardView cardViewDriver;

    private LinearLayout linearLayoutContact;


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
        txt_owner_reg_date = findViewById(R.id.txt_owner_reg_date);
        txt_day = findViewById(R.id.text_view_day);

        btnBookNow = findViewById(R.id.btnBookNow);

        cardViewDriver = findViewById(R.id.card_driver);

        linearLayoutContact = findViewById(R.id.linear_layout_contact);
        text = findViewById(R.id.text);
        call = findViewById(R.id.call);

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
        int vehicle_id = getIntent().getIntExtra("vehicle_id", 0);
        String vehicle_title = getIntent().getStringExtra("vehicle_title");
        String vehicle_overview = getIntent().getStringExtra("vehicle_overview");
        int price_per_day = getIntent().getIntExtra("price_per_day", 0);
        String powered_by = getIntent().getStringExtra("powered_by");
        String location = getIntent().getStringExtra("location");
        String model_year = getIntent().getStringExtra("model_year");
        String seating_capacity = getIntent().getStringExtra("seating_capacity");
        String driver_status = getIntent().getStringExtra("driver_status");
        String owner_id = getIntent().getStringExtra("owner_id");
        String reg_date = getIntent().getStringExtra("reg_date");
        String vehicle_brand = getIntent().getStringExtra("vehicle_brand");
        String booked = getIntent().getStringExtra("booked");
        String owner_phone_number = getIntent().getStringExtra("owner_phone_number");

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
        txt_vehicle_title.setText(vehicle_brand + " " + vehicle_title);
        txt_vehicle_overview.setText(vehicle_overview);

        //check if car is for sale
        if (booked.equals("10")){

            //format car price
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(price_per_day);

            txt_price_per_day.setText("Ksh " + formatted);
            txt_day.setVisibility(View.GONE);
            cardViewDriver.setVisibility(View.GONE);
            btnBookNow.setVisibility(View.GONE);
            linearLayoutContact.setVisibility(View.VISIBLE);

            //onclick listener phone call or text
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //request permission
                    requestSmsPermission();

                    //open sms
                    String number = owner_phone_number;  // The number on which you want to send SMS

                    Uri uri = Uri.parse("smsto:" + number);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(intent);


                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //check if permission is granted
                    int permissionCheck = ContextCompat.checkSelfPermission(CarDetailsActivity.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    CarDetailsActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    101);
                        } else {

                        //open dial pad
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + owner_phone_number));
                        startActivity(intent);

                        }


                }
            });



        }else {

            txt_price_per_day.setText("Ksh " + price_per_day);
            txt_driver_status.setText(driver_status);
            btnBookNow.setVisibility(View.VISIBLE);
            linearLayoutContact.setVisibility(View.GONE);

        }

        txt_powered_by.setText(powered_by);
        txt_location.setText(location);
        txt_model_year.setText(model_year);
        txt_seating_capacity.setText(seating_capacity);
        txt_owner_reg_date.setText(reg_date);

        //setimage resource
        if (airConditioner.equals("1")) {
            imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (antiLockBrakingSystem.equals("1")) {
            imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerSteering.equals("1")) {
            imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerWindows.equals("1")) {
            imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (cdPlayer.equals("1")) {
            imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (leatherSeats.equals("1")) {
            imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (centralLocking.equals("1")) {
            imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (powerDoorLocks.equals("1")) {
            imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (brakeAssist.equals("1")) {
            imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (driverAirbag.equals("1")) {
            imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (passengerAirbag.equals("1")) {
            imgPassengerAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgPassengerAirbag.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                    R.color.grey));
        }

        if (crashSensor.equals("1")) {
            imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
            imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
                    R.color.colorPrimary));


        }else {
            imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
            imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
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

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting the current user
                user user = SharedPrefManager.getInstance(CarDetailsActivity.this).getUser();

                Intent intent = new Intent(CarDetailsActivity.this, BookNowActivity.class);

                intent.putExtra("vehicle_id", vehicle_id);
                intent.putExtra("user_email", user.getEmail());
                intent.putExtra("price_per_day", price_per_day);
                intent.putExtra("vehicle_owner_email", owner_id);
                intent.putExtra("vehicle_title", vehicle_title);
                startActivity(intent);


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

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 101){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Call permission Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Call permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 102){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Sms permission Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Sms permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }

    }


}






