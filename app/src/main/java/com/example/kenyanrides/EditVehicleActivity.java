package com.example.kenyanrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditVehicleActivity extends AppCompatActivity {

    String brands_url = "https://kenyanrides.com/android/fetch_brands.php";

    List<String> brandsList = new ArrayList<>();
    List<Integer> brandsId = new ArrayList<Integer>();

    private Bitmap bitmap;
    String imageString;
    String image1Path;

    String image2String;
    String image2Path;

    String image3String;
    String image3Path;

    String image4String;
    String image4Path;

    String image5String;
    String image5Path;


    String[] fuel = {"Petrol", "Diesel", "Hybrid"};

    String[] seats = {"2 Seater", "4 Seater", "5 Seater", "7 Seater", "8 Seater",
            "9 Seater", "10 Seater", "11 Seater", "12 Seater", "13 Seater", "14 Seater",
            "18 Seater", "24 Seater", "32 Seater", "40 Seater", "56 Seater"};

    String[] driver_status_array = {"Self Driven", "Driver Inclusive"};
    String[] location_array = {"Baringo County", "Bomet County", "Bungoma County", "Busia County", "Elgeyo Marakwet County", "Embu County"
            , "Garissa County", "Homa Bay County", "Isiolo County", "Kajiado County", "Kakamega County", "Kericho County", "Kiambu County"
            , "Kilifi County", "Kirinyaga County", "Kisii County", "Kisumu County", "Kitui County", "Kwale County", "Laikipia County"
            , "Lamu County", "Machakos County", "Makueni County", "Mandera County", "Meru County", "Migori County", "Marsabit County"
            , "Mombasa County", "Muranga County", "Nairobi County", "Nakuru County", "Nandi County", "Narok County", "Nyamira County"
            , "Nyandarua County", "Nyeri County", "Samburu County", "Siaya County", "Taita Taveta County", "Tana River County", "Tharaka Nithi County"
            , "Trans Nzoia County", "Turkana County", "Uasin Gishu County", "Vihiga County", "Wajir County", "West Pokot County"};


    private TextView image1FilePath;
    private TextView image2FilePath;
    private TextView image3FilePath;
    private TextView image4FilePath;
    private TextView image5FilePath;

    public static final int IMAGE1 = 1;
    public static final int IMAGE2 = 2;
    public static final int IMAGE3 = 3;
    public static final int IMAGE4 = 4;
    public static final int IMAGE5 = 5;
    public static final int STORAGE_PERMISSION_CODE = 123;
    public static final String update_vehicle_url = "https://kenyanrides.com/android/update_vehicle.php";

    private EditText editTextVehicleOverview, editTextVehicleTitle, editTextPrice, editTextModelYear;

    Uri imageUri;
    String imagePath;
    Spinner brandSpinner;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog dialog;

    //upload strings
    String vehicleBrand;
    String vehicleFuel;
    String vehicleSeats;
    String vehicleDriverStatus;
    String vehicleLocation;

    String airConditioner;
    String powerDoorLocks;
    String antiLockBrakingSystem;
    String brakeAssist;
    String powerSteering;
    String driverAirBag;
    String passengerAirBag;
    String powerWindows;
    String cdPlayer;
    String centralLocking;
    String crashSensor;
    String leatherSeats;

    CheckBox checkBoxAirconditioner;
    CheckBox checkBoxPowerDoorLocks;
    CheckBox checkBoxAntiLockBrakingSystem;
    CheckBox checkBoxBrakeAssist;
    CheckBox checkBoxPowerSteering;
    CheckBox checkBoxDriverAirbag;
    CheckBox checkBoxPassengerAirbag;
    CheckBox checkBoxPowerWindows;
    CheckBox checkBoxCDPlayer;
    CheckBox checkBoxCentralLocking;
    CheckBox checkBoxCrashSensor;
    CheckBox checkBoxLeatherSeats;

    int vehicle_id;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        //get text from intent
        vehicle_id = getIntent().getIntExtra("vehicle_id", 0);
        String intent_vehicle_title = getIntent().getStringExtra("vehicle_title");
        String intent_vehicle_overview = getIntent().getStringExtra("vehicle_overview");
        int intent_price_per_day = getIntent().getIntExtra("price_per_day", 0);
        String intent_powered_by = getIntent().getStringExtra("powered_by");
        String intent_location = getIntent().getStringExtra("location");
        String intent_model_year = getIntent().getStringExtra("model_year");
        String intent_seating_capacity = getIntent().getStringExtra("seating_capacity");
        String intent_driver_status = getIntent().getStringExtra("driver_status");
        String intent_owner_id = getIntent().getStringExtra("owner_id");
        String reg_date = getIntent().getStringExtra("reg_date");

        String vimage1 = getIntent().getStringExtra("image1");
        String vimage2 = getIntent().getStringExtra("image2");
        String vimage3 = getIntent().getStringExtra("image3");
        String vimage4 = getIntent().getStringExtra("image4");
        String vimage5 = getIntent().getStringExtra("image5");

        String intent_airConditioner = getIntent().getStringExtra("airConditioner");
        String intent_powerDoorLocks = getIntent().getStringExtra("powerDoorLocks");
        String intent_antiLockBrakingSystem = getIntent().getStringExtra("antiLockBrakingSystem");
        String intent_brakeAssist = getIntent().getStringExtra("brakeAssist");
        String intent_powerSteering = getIntent().getStringExtra("powerSteering");
        String intent_driverAirbag = getIntent().getStringExtra("driverAirbag");
        String intent_passengerAirbag = getIntent().getStringExtra("passengerAirbag");
        String intent_powerWindows = getIntent().getStringExtra("powerWindows");
        String intent_cdPlayer = getIntent().getStringExtra("cdPlayer");
        String intent_centralLocking = getIntent().getStringExtra("centralLocking");
        String intent_crashSensor = getIntent().getStringExtra("crashSensor");
        String intent_leatherSeats = getIntent().getStringExtra("leatherSeats");

        alertDialogBuilder = new AlertDialog.Builder(this);

        dialog = new ProgressDialog(this);

        Button btnPostVehicle = findViewById(R.id.btnPostVehicle);

        //initialize views
        editTextVehicleTitle = findViewById(R.id.edit_text_vehicle_title);
        editTextVehicleOverview = findViewById(R.id.edit_text_vehicle_overview);
        editTextPrice = findViewById(R.id.edit_text_price);
        editTextModelYear = findViewById(R.id.edit_text_model_year);

        editTextVehicleTitle.setText(intent_vehicle_title);
        editTextVehicleOverview.setText(intent_vehicle_overview);
        editTextPrice.setText(String.valueOf(intent_price_per_day));
        editTextModelYear.setText(intent_model_year);

        //file chooser views
        TextView image1 = findViewById(R.id.image1);
        TextView image2 = findViewById(R.id.image2);
        TextView image3 = findViewById(R.id.image3);
        TextView image4 = findViewById(R.id.image4);
        TextView image5 = findViewById(R.id.image5);

        image1FilePath = findViewById(R.id.image1_file_path);
        image1FilePath.setText(vimage1.replace("https://kenyanrides.com/serviceprovider/img/vehicleimages/", " "));
        image2FilePath = findViewById(R.id.image2_file_path);
        image2FilePath.setText(vimage2.replace("https://kenyanrides.com/serviceprovider/img/vehicleimages/", ""));
        image3FilePath = findViewById(R.id.image3_file_path);
        image3FilePath.setText(vimage3.replace("https://kenyanrides.com/serviceprovider/img/vehicleimages/", ""));
        image4FilePath = findViewById(R.id.image4_file_path);
        image4FilePath.setText(vimage4.replace("https://kenyanrides.com/serviceprovider/img/vehicleimages/", ""));
        image5FilePath = findViewById(R.id.image5_file_path);
        image5FilePath.setText(vimage5.replace("https://kenyanrides.com/serviceprovider/img/vehicleimages/", ""));

        //file chooser click listener
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, IMAGE1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image2Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image2Intent.setType("image/*");
                image2Intent = Intent.createChooser(image2Intent, "Choose a file");
                startActivityForResult(image2Intent, IMAGE2);

            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image3Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image3Intent.setType("image/*");
                image3Intent = Intent.createChooser(image3Intent, "Choose a file");
                startActivityForResult(image3Intent, IMAGE3);

            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image2Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image2Intent.setType("image/*");
                image2Intent = Intent.createChooser(image2Intent, "Choose a file");
                startActivityForResult(image2Intent, IMAGE4);

            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image2Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image2Intent.setType("image/*");
                image2Intent = Intent.createChooser(image2Intent, "Choose a file");
                startActivityForResult(image2Intent, IMAGE5);

            }
        });

        //spinner initialization
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner fuelSpinner = findViewById(R.id.fuel_spinner);

        final Spinner seatsSpinner = findViewById(R.id.seats_spinner);

        final Spinner driverSpinner = findViewById(R.id.driver_status_spinner);

        Spinner locationSpinner = findViewById(R.id.location_spinner);

        brandSpinner = findViewById(R.id.brand_spinner);

        //initialize check boxes
        checkBoxAirconditioner = findViewById(R.id.chkbox_airconditioner);
        checkBoxPowerDoorLocks = findViewById(R.id.chkbox_power_door_locks);
        checkBoxAntiLockBrakingSystem = findViewById(R.id.chkbox_anti_lock_braking_system);
        checkBoxBrakeAssist = findViewById(R.id.chkbox_brake_assist);
        checkBoxPowerSteering = findViewById(R.id.chkbox_power_steering);
        checkBoxDriverAirbag = findViewById(R.id.chkbox_driver_airbag);
        checkBoxPassengerAirbag = findViewById(R.id.chkbox_passenger_airbag);
        checkBoxPowerWindows = findViewById(R.id.chkbox_power_windows);
        checkBoxCDPlayer = findViewById(R.id.chkbox_cdplayer);
        checkBoxCentralLocking = findViewById(R.id.chkbox_central_locking);
        checkBoxCrashSensor = findViewById(R.id.chkbox_crash_sensor);
        checkBoxLeatherSeats = findViewById(R.id.chkbox_leather_seats);

        //set check boxes values
        if(intent_airConditioner.equals("1")){
            checkBoxAirconditioner.setChecked(true);
        }

        if(intent_powerDoorLocks.equals("1")){
            checkBoxPowerDoorLocks.setChecked(true);
        }

        if(intent_antiLockBrakingSystem.equals("1")){
            checkBoxAntiLockBrakingSystem.setChecked(true);
        }

        if(intent_brakeAssist.equals("1")){
            checkBoxBrakeAssist.setChecked(true);
        }

        if(intent_powerSteering.equals("1")){
            checkBoxPowerSteering.setChecked(true);
        }

        if(intent_driverAirbag.equals("1")){
            checkBoxDriverAirbag.setChecked(true);
        }

        if(intent_passengerAirbag.equals("1")){
            checkBoxPassengerAirbag.setChecked(true);
        }

        if(intent_powerWindows.equals("1")){
            checkBoxPowerWindows.setChecked(true);
        }

        if(intent_cdPlayer.equals("1")){
            checkBoxCDPlayer.setChecked(true);
        }

        if(intent_centralLocking.equals("1")){
            checkBoxCentralLocking.setChecked(true);
        }

        if(intent_crashSensor.equals("1")){
            checkBoxCrashSensor.setChecked(true);
        }

        if(intent_leatherSeats.equals("1")){
            checkBoxLeatherSeats.setChecked(true);
        }

        fuelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {

                    vehicleFuel = item.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,fuel);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        fuelSpinner.setAdapter(aa);

        int spinnerPosition = aa.getPosition(intent_powered_by);
        fuelSpinner.setSelection(spinnerPosition);

        seatsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {

                    String s = item.toString();
                    vehicleSeats = s.replace(" Seater", "");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter seatsAdapter = new ArrayAdapter(this,R.layout.spinner_item,seats);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        seatsSpinner.setAdapter(seatsAdapter);

        int seatsposition = seatsAdapter.getPosition(intent_seating_capacity);
        seatsSpinner.setSelection(seatsposition);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {

                    vehicleLocation =item.toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter locationAdapter = new ArrayAdapter(this,R.layout.spinner_item,location_array);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationSpinner.setAdapter(locationAdapter);

        //set default item
        int locationposition = locationAdapter.getPosition(intent_location);
        locationSpinner.setSelection(locationposition);

        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {
                    vehicleDriverStatus = item.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter driverAdapter = new ArrayAdapter(this,R.layout.spinner_item,driver_status_array);
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        driverSpinner.setAdapter(driverAdapter);

        //set default item
        int driverposition = driverAdapter.getPosition(intent_driver_status);
        driverSpinner.setSelection(driverposition);

        EditVehicleActivity.BackTask backTask =new EditVehicleActivity.BackTask();
        backTask.execute();

        btnPostVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postVehicle();
            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE1:
                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image1FilePath.setText(getFileName(imageUri));

                image1Path = getPath(imageUri);


                break;

            case IMAGE2:

                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image2FilePath.setText(getFileName(imageUri));

                image2Path = getPath(imageUri);


                break;

            case IMAGE3:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image3FilePath.setText(getFileName(imageUri));

                image3Path = getPath(imageUri);


                break;

            case IMAGE4:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image4FilePath.setText(getFileName(imageUri));

                image4Path = getPath(imageUri);

                break;

            case IMAGE5:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image5FilePath.setText(getFileName(imageUri));

                image5Path = getPath(imageUri);


                break;


        }


    }



    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getPath(Uri uri){
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String document_id = cursor.getString(0);

        document_id =document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor != null && cursor.moveToFirst()) {

            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }


        return path;
    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    //on click for button to post vehicles
    public void postVehicle(){


        //check if network is connected
        if (!isNetworkAvailable()){

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.show();
            return;
        }

        //get edit text strings
        String vehicleTitle = editTextVehicleTitle.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(vehicleTitle)){
            editTextVehicleTitle.setError("Vehicle Title cannot be empty");
            return;
        }

        //get edit text strings
        String vehicleOverview = editTextVehicleOverview.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(vehicleOverview)){
            editTextVehicleOverview.setError("Vehicle Overview cannot be empty");
            return;
        }


        //check text
        String vehiclePrice = editTextPrice.getText().toString();
        //check if its empty
        if(TextUtils.isEmpty(vehiclePrice) ){
            editTextPrice.setError("Vehicle price cannot be empty");
            return;
        } else{
            double price = 1.1 * Integer.parseInt(vehiclePrice);

            vehiclePrice = String.valueOf(price);
        }

        //get edit text strings
        String vehicleModelYear = editTextModelYear.getText().toString();

        //check if its empty
        if(TextUtils.isEmpty(vehicleModelYear)){
            editTextModelYear.setError("Vehicle Model Year cannot be empty");
            return;
        }


        //checkbox initialization and saving value of checked

        if (checkBoxAirconditioner.isChecked()){
            airConditioner = "1";

        }else {
            airConditioner = "NULL";
        }


        if (checkBoxPowerDoorLocks.isChecked()){
            powerDoorLocks = "1";

        }else {
            powerDoorLocks = "NULL";
        }

        if (checkBoxAntiLockBrakingSystem.isChecked()){
            antiLockBrakingSystem = "1";

        }else {
            antiLockBrakingSystem = "NULL";
        }

        if (checkBoxBrakeAssist.isChecked()){
            brakeAssist = "1";

        }else {
            brakeAssist = "NULL";
        }

        if (checkBoxPowerSteering.isChecked()){
            powerSteering = "1";

        }else {
            powerSteering = "NULL";
        }

        if (checkBoxDriverAirbag.isChecked()){
            driverAirBag = "1";

        }else {
            driverAirBag = "NULL";
        }

        if (checkBoxPassengerAirbag.isChecked()){
            passengerAirBag = "1";

        }else {
            passengerAirBag = "NULL";
        }

        if (checkBoxPowerWindows.isChecked()){
            powerWindows = "1";

        }else {
            powerWindows = "NULL";
        }

        if (checkBoxCDPlayer.isChecked()){
            cdPlayer = "1";

        }else {
            cdPlayer = "NULL";
        }

        if (checkBoxCentralLocking.isChecked()){
            centralLocking = "1";

        }else {
            centralLocking = "NULL";
        }

        if (checkBoxCrashSensor.isChecked()){
            crashSensor = "1";

        }else {
            crashSensor = "NULL";
        }

        if (checkBoxLeatherSeats.isChecked()){
            leatherSeats = "1";

        }else {
            leatherSeats = "NULL";
        }

        //check that images are selected
        if (image1FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image2FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image3FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image4FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image5FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }


        try{

            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();



            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, uploadId, update_vehicle_url)
                    .addFileToUpload(image1Path, "image1")
                    .addFileToUpload(image2Path, "image2")
                    .addFileToUpload(image2Path, "image3")
                    .addFileToUpload(image4Path, "image4")
                    .addFileToUpload(image5Path, "image5")
                    .addParameter("vehicle_title", vehicleTitle)
                    .addParameter("vehicle_brand", vehicleBrand)
                    .addParameter("vehicle_overview", vehicleOverview)
                    .addParameter("vehicle_price", vehiclePrice)
                    .addParameter("fuel", vehicleFuel)
                    .addParameter("vehicle_location", vehicleLocation)
                    .addParameter("vehicle_model_year", vehicleModelYear)
                    .addParameter("vehicle_seats", vehicleSeats)
                    .addParameter("vehicle_driver_status", vehicleDriverStatus)
                    .addParameter("airconditioner", airConditioner)
                    .addParameter("powerdoorlocks", powerDoorLocks)
                    .addParameter("antilockbrakingsystem", antiLockBrakingSystem)
                    .addParameter("brakeassist", brakeAssist)
                    .addParameter("powersteering", powerSteering)
                    .addParameter("driverairbag", driverAirBag)
                    .addParameter("passengerairbag", passengerAirBag)
                    .addParameter("powerwindows", powerWindows)
                    .addParameter("cdplayer", cdPlayer)
                    .addParameter("centrallocking", centralLocking)
                    .addParameter("crashsensor", crashSensor)
                    .addParameter("leatherseats", leatherSeats)
                    .addParameter("vehicleId", String.valueOf(vehicle_id))
                    .addParameter("booked", "1")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            dialog.setMessage("Loading...");
                            dialog.show();

                            Toast.makeText(context, String.valueOf(uploadInfo) , Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Toast.makeText(context, String.valueOf(exception) , Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            dialog.dismiss();

                            switch (String.valueOf(serverResponse)){
                                case "File is valid, and was successfully uploaded":
                                    alertDialogBuilder.setTitle("Success!");
                                    alertDialogBuilder.setMessage("Vehicle was uploaded succesfully");
                                    alertDialogBuilder.show();
                                    break;

                                case "Upload failed":
                                    alertDialogBuilder.setTitle("Failed!");
                                    alertDialogBuilder.setMessage("Vehicle was not uploaded! Please try uploading again");
                                    alertDialogBuilder.show();
                                    break;
                            }

                            Toast.makeText(context, String.valueOf(serverResponse) , Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                            Toast.makeText(context, String.valueOf(uploadInfo) , Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }
                    })
                    .startUpload();



        }catch (Exception e){

        }


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //fetch brands from db


    private class BackTask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditVehicleActivity.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL(brands_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader inputStream = new InputStreamReader( httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStream);

                StringBuilder builder = new StringBuilder();

                String line;

                while((line = bufferedReader.readLine()) != null ){

                    builder.append(line);

                }

                //assign json data collected
                result = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //get json array from result passed
                JSONArray array = new JSONArray(result);

                //loop through all json objects in json array
                for(int i = 0; i<array.length();i++){

                    //each object
                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("id");

                    String brandName = object.getString("Brand");


                    brandsList.add(brandName);
                    brandsId.add(id);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.e("Error3", "3");

            //Creating the ArrayAdapter instance having the bank name list
            ArrayAdapter brandAdapter = new ArrayAdapter(EditVehicleActivity.this,R.layout.spinner_item,brandsList);
            brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            brandSpinner.setAdapter(brandAdapter);

            brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    Object item = adapterView.getItemAtPosition(i);
                    if (item != null) {

                        vehicleBrand = brandsId.get(i).toString();


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }
    }


}