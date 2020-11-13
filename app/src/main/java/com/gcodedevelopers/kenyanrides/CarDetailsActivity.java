package com.gcodedevelopers.kenyanrides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CarDetailsActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static String[] IMAGES ;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    private TextView txt_vehicle_title, txt_vehicle_overview, txt_price_per_day, txt_powered_by,
            txt_location, txt_model_year, txt_seating_capacity, txt_driver_status, txt_owner_reg_date, txt_day;

    private LinearLayout text, call;

    private ImageView imgAirConditioner, imgAntilockBrakingSystem, imgPowerSteering, imgPowerWindows, imgCdPlayer, imgLeatherSeats, imgCentralLocking,
    imgPowerDoorLocks, imgBrakeAssist, imgDriverAirbag, imgPassengerAirbag, imgCrashSensor;

    private Button btnBookNow;

    private CardView cardViewDriver;

    private LinearLayout linearLayoutContact, linearLayoutVideoView;

    private ImageView imgShareButton;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog progressDialog;

    private static final String vehicles_url = "https://kenyanrides.com/android/deep_link_api.php";

    //video view
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaControls;

    private String carId;

    private String booked = "";

    private String airConditioner = "";
    private String powerDoorLocks = "";
    private String antiLockBrakingSystem = "";
    private String brakeAssist = "";
    private String powerSteering = "";
    private String driverAirbag = "";
    private String passengerAirbag = "";
    private String powerWindows = "";
    private String cdPlayer = "";
    private String centralLocking = "";
    private String crashSensor = "";
    private String leatherSeats = "";

    private String vehicle_title, vehicle_overview, powered_by, location, model_year, seating_capacity, driver_status,
            owner_id, reg_date, vehicle_brand, owner_phone_number,car_video,
            image1, image2, image3, image4, image5;

    private int vehicle_id, price_per_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);


        Uri uri = getIntent().getData();
        if (uri != null){

            carId = uri.getQueryParameter("id");

            progressDialog = new ProgressDialog(this);

            alertDialogBuilder = new AlertDialog.Builder(this);

            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //check if network is connected
            if (!isNetworkAvailable()){
                progressDialog.dismiss();

                alertDialogBuilder.setTitle("Network Failure");
                alertDialogBuilder.setMessage("Please check your internet connection!");
                alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        prepareCars();

                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, vehicles_url, response -> {

                try {
                    JSONArray carsJson = new JSONArray(response);

                    //check if no cars are available
                    if(carsJson.length() != 0){

                        for(int i = 0; i<carsJson.length(); i++) {

                            //get json objects
                            JSONObject carsObject = carsJson.getJSONObject(i);

                            vehicle_title = carsObject.getString("VehiclesTitle");

                            price_per_day = carsObject.getInt("PricePerDay");

                            vehicle_id = carsObject.getInt("id");
                            vehicle_brand = carsObject.getString("brandName");
                            vehicle_overview = carsObject.getString("VehiclesOverview");
                            powered_by = carsObject.getString("poweredby");
                            location = carsObject.getString("FuelType");
                            model_year = carsObject.getString("ModelYear");
                            seating_capacity = carsObject.getString("SeatingCapacity");
                            driver_status = carsObject.getString("Dstatus");
                            String vehicleImage = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage1");
                            image1 = vehicleImage.replace(" ", "%20");
                            String vehicleImage2 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage2");
                            image2 = vehicleImage2.replace(" ", "%20");
                            String vehicleImage3 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage3");
                            image3 = vehicleImage3.replace(" ", "%20");
                            String vehicleImage4 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage4");
                            image4 = vehicleImage4.replace(" ", "%20");
                            String vehicleImage5 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage5");
                            image5 = vehicleImage5.replace(" ", "%20");
                            airConditioner = carsObject.getString("AirConditioner");
                            powerDoorLocks = carsObject.getString("PowerDoorLocks");
                            antiLockBrakingSystem = carsObject.getString("AntiLockBrakingSystem");
                            brakeAssist = carsObject.getString("BrakeAssist");
                            powerSteering = carsObject.getString("PowerSteering");
                            driverAirbag = carsObject.getString("DriverAirbag");
                            passengerAirbag = carsObject.getString("PassengerAirbag");
                            powerWindows = carsObject.getString("PowerWindows");
                            cdPlayer = carsObject.getString("CDPlayer");
                            centralLocking = carsObject.getString("CentralLocking");
                            crashSensor = carsObject.getString("CrashSensor");
                            leatherSeats = carsObject.getString("LeatherSeats");
                            owner_id = carsObject.getString("owner_id");
                            reg_date = carsObject.getString("RegDate");
                            booked = carsObject.getString("booked");
                            owner_phone_number = carsObject.getString("owner_phonenumber");
                            car_video = carsObject.getString("car_video");

                            //************************************************************************************************************************//

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
                            videoView = findViewById(R.id.video_view);


                            btnBookNow = findViewById(R.id.btnBookNow);

                            cardViewDriver = findViewById(R.id.card_driver);

                            linearLayoutContact = findViewById(R.id.linear_layout_contact);

                            linearLayoutVideoView = findViewById(R.id.videoViewLayout);

                            linearLayoutVideoView.setVisibility(View.GONE);

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


                            imgShareButton = findViewById(R.id.imgShareButton);
                            imgShareButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout " + vehicle_brand + " " + vehicle_title + " on KenyanRides app " + "https://www.kenyanrides.com/app.html?id=" + vehicle_id);
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);

                                }
                            });

                            //set text to their fields
                            txt_vehicle_title.setText(vehicle_brand + " " + vehicle_title);
                            txt_vehicle_overview.setText(vehicle_overview);


                            //check if car is for sale
                            if (booked.equals("10") && !booked.equals("")){

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

                                        //open sms
                                        String number = owner_phone_number;  // The number on which you want to send SMS

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
                                        startActivity(intent);


                                    }
                                });

                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //open dial pad

                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + owner_phone_number));
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }


                                    }
                                });

                                //if car video exists
                                if (!car_video.equals("null")){

                                    String video_url = "https://kenyanrides.com/serviceprovider/img/video/" + car_video;
                                    String videoUrlEditted = video_url.replace(" ", "%20");

                                    //show video if car has video
                                    linearLayoutVideoView.setVisibility(View.VISIBLE);


                                    // set the media controller buttons
                                    if (mediaControls == null)
                                    {
                                        mediaControls = new MediaController(CarDetailsActivity.this);
                                    }


                                    try
                                    {

                                        // set the media controller in the VideoView
                                        videoView.setMediaController(mediaControls);

                                        // set the url of the video to be played
                                        videoView.setVideoPath(videoUrlEditted);

                                    } catch (Exception e)
                                    {
                                        Log.e("Error", e.getMessage());
                                        e.printStackTrace();
                                    }

                                    //videoView.requestFocus();

                                    // we also set an setOnPreparedListener in order to know when the video
                                    // file is ready for playback

                                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                                    {

                                        public void onPrepared(MediaPlayer mediaPlayer)
                                        {
                                            // if we have a position on savedInstanceState, the video
                                            // playback should start from here
                                            videoView.seekTo(position);

                                            if (position == 0)
                                            {
                                                videoView.start();
                                            } else
                                            {
                                                // if we come from a resumed activity, video playback will
                                                // be paused
                                                videoView.pause();
                                            }
                                        }
                                    });

                                }else {
                                    linearLayoutVideoView.setVisibility(View.GONE);
                                }

                                if (!car_video.isEmpty()){

                                    String video_url = "https://kenyanrides.com/serviceprovider/img/video/" + car_video;
                                    String videoUrlEditted = video_url.replace(" ", "%20");

                                    //show video if car has video
                                    linearLayoutVideoView.setVisibility(View.VISIBLE);


                                    // set the media controller buttons
                                    if (mediaControls == null)
                                    {
                                        mediaControls = new MediaController(CarDetailsActivity.this);
                                    }


                                    try
                                    {

                                        // set the media controller in the VideoView
                                        videoView.setMediaController(mediaControls);

                                        // set the url of the video to be played
                                        videoView.setVideoPath(videoUrlEditted);

                                    } catch (Exception e)
                                    {
                                        Log.e("Error", e.getMessage());
                                        e.printStackTrace();
                                    }

                                    //videoView.requestFocus();

                                    // we also set an setOnPreparedListener in order to know when the video
                                    // file is ready for playback

                                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                                    {

                                        public void onPrepared(MediaPlayer mediaPlayer)
                                        {
                                            // if we have a position on savedInstanceState, the video
                                            // playback should start from here
                                            videoView.seekTo(position);

                                            if (position == 0)
                                            {
                                                videoView.pause();
                                            } else
                                            {
                                                // if we come from a resumed activity, video playback will
                                                // be paused
                                                videoView.pause();
                                            }
                                        }
                                    });


                                }else {
                                    linearLayoutVideoView.setVisibility(View.GONE);
                                }


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
                            if (airConditioner.equals("1") && !airConditioner.isEmpty()) {
                                imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (antiLockBrakingSystem.equals("1") && !antiLockBrakingSystem.isEmpty()) {
                                imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (powerSteering.equals("1") && !powerSteering.isEmpty()) {
                                imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (powerWindows.equals("1") && !powerWindows.isEmpty()) {
                                imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (cdPlayer.equals("1") && !cdPlayer.isEmpty()) {
                                imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (leatherSeats.equals("1") && !leatherSeats.isEmpty()) {
                                imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (centralLocking.equals("1") && !centralLocking.isEmpty()) {
                                imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (powerDoorLocks.equals("1") && !powerDoorLocks.isEmpty()) {
                                imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (brakeAssist.equals("1") && !brakeAssist.isEmpty()) {
                                imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (driverAirbag.equals("1") && !driverAirbag.isEmpty()) {
                                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (passengerAirbag.equals("1") && !passengerAirbag.isEmpty()) {
                                imgPassengerAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgPassengerAirbag.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }

                            if (crashSensor.equals("1") && !crashSensor.isEmpty()) {
                                imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                                imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
                                        R.color.colorPrimary));


                            }else {
                                imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                                imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
                                        R.color.grey));
                            }


                            IMAGES = new String[]{image1, image2, image3, image4, image5};


                            init();

                            btnBookNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!SharedPrefManager.getInstance(CarDetailsActivity.this).isLoggedIn()) {

                                        Intent intent = new Intent(CarDetailsActivity.this, LoginActivity.class);

                                        intent.putExtra("deep_link", "1");
                                        intent.putExtra("vehicle_id", String.valueOf(vehicle_id));
                                        intent.putExtra("price_per_day", String.valueOf(price_per_day));
                                        intent.putExtra("vehicle_owner_email", owner_id);
                                        intent.putExtra("vehicle_title", vehicle_title);
                                        intent.putExtra("owner_phone_number", owner_phone_number);

                                        startActivity(intent);

                                    } else {

                                        //getting the current user
                                        user user = SharedPrefManager.getInstance(CarDetailsActivity.this).getUser();

                                        Intent intent = new Intent(CarDetailsActivity.this, BookNowActivity.class);

                                        intent.putExtra("vehicle_id", String.valueOf(vehicle_id));
                                        intent.putExtra("user_email", user.getEmail());
                                        intent.putExtra("price_per_day", String.valueOf(price_per_day));
                                        intent.putExtra("vehicle_owner_email", owner_id);
                                        intent.putExtra("vehicle_title", vehicle_title);
                                        intent.putExtra("owner_phone_number", owner_phone_number);

                                        startActivity(intent);

                                    }

                                }
                            });



                            //************************************************************************************************************************//


                        }

                    }else {

                        alertDialogBuilder.setTitle("Error");
                        alertDialogBuilder.setMessage("The vehicle does not exist or has been deleted.");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(CarDetailsActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                        });

                        alertDialogBuilder.show();

                    }

                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();
                    alertDialogBuilder.setTitle("Error occurred");
                    alertDialogBuilder.setMessage("Please ensure you have stable internet connection!");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(CarDetailsActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    alertDialogBuilder.show();

                }
            }){
                //send params needed to db
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("vehicle_id", carId);

                    return params;

                }
            };

            Volley.newRequestQueue(CarDetailsActivity.this).add(stringRequest);


        }else{

            //get text from intent
            vehicle_id = getIntent().getIntExtra("vehicle_id", 0);
            vehicle_title = getIntent().getStringExtra("vehicle_title");
            vehicle_overview = getIntent().getStringExtra("vehicle_overview");
            price_per_day = getIntent().getIntExtra("price_per_day", 0);
            powered_by = getIntent().getStringExtra("powered_by");
            location = getIntent().getStringExtra("location");
            model_year = getIntent().getStringExtra("model_year");
            seating_capacity = getIntent().getStringExtra("seating_capacity");
            driver_status = getIntent().getStringExtra("driver_status");
            owner_id = getIntent().getStringExtra("owner_id");
            reg_date = getIntent().getStringExtra("reg_date");
            vehicle_brand = getIntent().getStringExtra("vehicle_brand");
            booked = getIntent().getStringExtra("booked");
            owner_phone_number = getIntent().getStringExtra("owner_phone_number");

            airConditioner = getIntent().getStringExtra("airConditioner");
            powerDoorLocks = getIntent().getStringExtra("powerDoorLocks");
            antiLockBrakingSystem = getIntent().getStringExtra("antiLockBrakingSystem");
            brakeAssist = getIntent().getStringExtra("brakeAssist");
            powerSteering = getIntent().getStringExtra("powerSteering");
            driverAirbag = getIntent().getStringExtra("driverAirbag");
            passengerAirbag = getIntent().getStringExtra("passengerAirbag");
            powerWindows = getIntent().getStringExtra("powerWindows");
            cdPlayer = getIntent().getStringExtra("cdPlayer");
            centralLocking = getIntent().getStringExtra("centralLocking");
            crashSensor = getIntent().getStringExtra("crashSensor");
            leatherSeats = getIntent().getStringExtra("leatherSeats");
            car_video = getIntent().getStringExtra("car_video");

            //get images from intent
            image1 = getIntent().getStringExtra("image1");
            image2 = getIntent().getStringExtra("image2");
            image3 = getIntent().getStringExtra("image3");
            image4 = getIntent().getStringExtra("image4");
            image5 = getIntent().getStringExtra("image5");

            //************************************************************************************//

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
            videoView = findViewById(R.id.video_view);


            btnBookNow = findViewById(R.id.btnBookNow);

            cardViewDriver = findViewById(R.id.card_driver);

            linearLayoutContact = findViewById(R.id.linear_layout_contact);

            linearLayoutVideoView = findViewById(R.id.videoViewLayout);

            linearLayoutVideoView.setVisibility(View.GONE);

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


            imgShareButton = findViewById(R.id.imgShareButton);
            imgShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, \n Checkout " + vehicle_brand + " " + vehicle_title + " available on KenyanRides app " + "https://www.kenyanrides.com/app.html?id=" + vehicle_id);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                }
            });

            //set text to their fields
            txt_vehicle_title.setText(vehicle_brand + " " + vehicle_title);
            txt_vehicle_overview.setText(vehicle_overview);


            //check if car is for sale
            if (booked.equals("10") && !booked.equals("")){

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

                        //open sms
                        String number = owner_phone_number;  // The number on which you want to send SMS

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
                        startActivity(intent);


                    }
                });

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //open dial pad

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + owner_phone_number));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }


                    }
                });

                //if car video exists
                if (!car_video.equals("null")){

                    String video_url = "https://kenyanrides.com/serviceprovider/img/video/" + car_video;
                    String videoUrlEditted = video_url.replace(" ", "%20");

                    //show video if car has video
                    linearLayoutVideoView.setVisibility(View.VISIBLE);


                    // set the media controller buttons
                    if (mediaControls == null)
                    {
                        mediaControls = new MediaController(CarDetailsActivity.this);
                    }


                    try
                    {

                        // set the media controller in the VideoView
                        videoView.setMediaController(mediaControls);

                        // set the url of the video to be played
                        videoView.setVideoPath(videoUrlEditted);

                    } catch (Exception e)
                    {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    //videoView.requestFocus();

                    // we also set an setOnPreparedListener in order to know when the video
                    // file is ready for playback

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                    {

                        public void onPrepared(MediaPlayer mediaPlayer)
                        {
                            // if we have a position on savedInstanceState, the video
                            // playback should start from here
                            videoView.seekTo(position);

                            if (position == 0)
                            {
                                videoView.start();
                            } else
                            {
                                // if we come from a resumed activity, video playback will
                                // be paused
                                videoView.pause();
                            }
                        }
                    });

                }else {
                    linearLayoutVideoView.setVisibility(View.GONE);
                }

                if (!car_video.isEmpty()){

                    String video_url = "https://kenyanrides.com/serviceprovider/img/video/" + car_video;
                    String videoUrlEditted = video_url.replace(" ", "%20");

                    //show video if car has video
                    linearLayoutVideoView.setVisibility(View.VISIBLE);


                    // set the media controller buttons
                    if (mediaControls == null)
                    {
                        mediaControls = new MediaController(CarDetailsActivity.this);
                    }


                    try
                    {

                        // set the media controller in the VideoView
                        videoView.setMediaController(mediaControls);

                        // set the url of the video to be played
                        videoView.setVideoPath(videoUrlEditted);

                    } catch (Exception e)
                    {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    //videoView.requestFocus();

                    // we also set an setOnPreparedListener in order to know when the video
                    // file is ready for playback

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                    {

                        public void onPrepared(MediaPlayer mediaPlayer)
                        {
                            // if we have a position on savedInstanceState, the video
                            // playback should start from here
                            videoView.seekTo(position);

                            if (position == 0)
                            {
                                videoView.pause();
                            } else
                            {
                                // if we come from a resumed activity, video playback will
                                // be paused
                                videoView.pause();
                            }
                        }
                    });


                }else {
                    linearLayoutVideoView.setVisibility(View.GONE);
                }


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
            if (airConditioner.equals("1") && !airConditioner.isEmpty()) {
                imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgAirConditioner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgAirConditioner.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (antiLockBrakingSystem.equals("1") && !antiLockBrakingSystem.isEmpty()) {
                imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgAntilockBrakingSystem.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgAntilockBrakingSystem.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (powerSteering.equals("1") && !powerSteering.isEmpty()) {
                imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgPowerSteering.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgPowerSteering.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (powerWindows.equals("1") && !powerWindows.isEmpty()) {
                imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgPowerWindows.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgPowerWindows.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (cdPlayer.equals("1") && !cdPlayer.isEmpty()) {
                imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgCdPlayer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgCdPlayer.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (leatherSeats.equals("1") && !leatherSeats.isEmpty()) {
                imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgLeatherSeats.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgLeatherSeats.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (centralLocking.equals("1") && !centralLocking.isEmpty()) {
                imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgCentralLocking.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgCentralLocking.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (powerDoorLocks.equals("1") && !powerDoorLocks.isEmpty()) {
                imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgPowerDoorLocks.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgPowerDoorLocks.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (brakeAssist.equals("1") && !brakeAssist.isEmpty()) {
                imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgBrakeAssist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgBrakeAssist.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (driverAirbag.equals("1") && !driverAirbag.isEmpty()) {
                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (passengerAirbag.equals("1") && !passengerAirbag.isEmpty()) {
                imgPassengerAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgPassengerAirbag.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgDriverAirbag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgDriverAirbag.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }

            if (crashSensor.equals("1") && !crashSensor.isEmpty()) {
                imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_checked_box));
                imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary));


            }else {
                imgCrashSensor.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_unchecked_box));
                imgCrashSensor.setColorFilter(ContextCompat.getColor(this,
                        R.color.grey));
            }


            IMAGES = new String[]{image1, image2, image3, image4, image5};


            init();

            btnBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getting the current user
                    user user = SharedPrefManager.getInstance(CarDetailsActivity.this).getUser();

                    Intent intent = new Intent(CarDetailsActivity.this, BookNowActivity.class);

                    intent.putExtra("vehicle_id", String.valueOf(vehicle_id));
                    intent.putExtra("user_email", user.getEmail());
                    intent.putExtra("price_per_day", String.valueOf(price_per_day));
                    intent.putExtra("vehicle_owner_email", owner_id);
                    intent.putExtra("vehicle_title", vehicle_title);
                    intent.putExtra("owner_phone_number", owner_phone_number);

                    startActivity(intent);


                }
            });


            //***********************************************************************************//

        }


    }
//
    private void init(){

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.requestFocus();

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


    private void prepareCars() {

        progressDialog = new ProgressDialog(this);

        alertDialogBuilder = new AlertDialog.Builder(this);

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //check if network is connected
        if (!isNetworkAvailable()){
            progressDialog.dismiss();

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    prepareCars();

                }
            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, vehicles_url, response -> {

            Toast.makeText(this, "hapa", Toast.LENGTH_SHORT).show();

            try {
                JSONArray carsJson = new JSONArray(response);

                //check if no cars are available
                if(carsJson.length() != 0){

                    for(int i = 0; i<carsJson.length(); i++) {

                        //get json objects
                        JSONObject carsObject = carsJson.getJSONObject(i);

                        vehicle_title = carsObject.getString("VehiclesTitle");

                        price_per_day = carsObject.getInt("PricePerDay");

                        vehicle_id = carsObject.getInt("id");
                        vehicle_brand = carsObject.getString("brandName");
                        vehicle_overview = carsObject.getString("VehiclesOverview");
                        powered_by = carsObject.getString("poweredby");
                        location = carsObject.getString("FuelType");
                        model_year = carsObject.getString("ModelYear");
                        seating_capacity = carsObject.getString("SeatingCapacity");
                        driver_status = carsObject.getString("Dstatus");
                        String vehicleImage = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage1");
                        image1 = vehicleImage.replace(" ", "%20");
                        String vehicleImage2 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage2");
                        image2 = vehicleImage2.replace(" ", "%20");
                        String vehicleImage3 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage3");
                        image3 = vehicleImage3.replace(" ", "%20");
                        String vehicleImage4 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage4");
                        image4 = vehicleImage4.replace(" ", "%20");
                        String vehicleImage5 = "https://kenyanrides.com/serviceprovider/img/vehicleimages/" + carsObject.getString("Vimage5");
                        image5 = vehicleImage5.replace(" ", "%20");
                        airConditioner = carsObject.getString("AirConditioner");
                        powerDoorLocks = carsObject.getString("PowerDoorLocks");
                        antiLockBrakingSystem = carsObject.getString("AntiLockBrakingSystem");
                        brakeAssist = carsObject.getString("BrakeAssist");
                        powerSteering = carsObject.getString("PowerSteering");
                        driverAirbag = carsObject.getString("DriverAirbag");
                        passengerAirbag = carsObject.getString("PassengerAirbag");
                        powerWindows = carsObject.getString("PowerWindows");
                        cdPlayer = carsObject.getString("CDPlayer");
                        centralLocking = carsObject.getString("CentralLocking");
                        crashSensor = carsObject.getString("CrashSensor");
                        leatherSeats = carsObject.getString("LeatherSeats");
                        owner_id = carsObject.getString("owner_id");
                        reg_date = carsObject.getString("RegDate");
                        booked = carsObject.getString("booked");
                        owner_phone_number = carsObject.getString("owner_phonenumber");
                        car_video = carsObject.getString("car_video");

                        Toast.makeText(this, booked, Toast.LENGTH_SHORT).show();

                    }

                }else {

                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage("The vehicle does not exist or has been deleted.");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(CarDetailsActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    });

                    alertDialogBuilder.show();

                }

                progressDialog.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CarDetailsActivity.this, "hapa2", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
                alertDialogBuilder.setTitle("Error occurred");
                alertDialogBuilder.setMessage("Please ensure you have stable internet connection!");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        prepareCars();
                    }
                });

                alertDialogBuilder.show();

            }
        }){
            //send params needed to db
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vehicle_id", carId);

                return params;

            }
        };

        Volley.newRequestQueue(CarDetailsActivity.this).add(stringRequest);


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}






