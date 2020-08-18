
package com.example.kenyanrides;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellFragment extends Fragment  {

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


    String[] fuel={"SELECT", "Petrol","Diesel","Hybrid"};

    String[] seats={"SELECT", "2 Seater", "4 Seater", "5 Seater", "7 Seater", "8 Seater",
            "9 Seater", "10 Seater", "11 Seater", "12 Seater", "13 Seater", "14 Seater",
            "18 Seater", "24 Seater", "32 Seater", "40 Seater", "56 Seater"};

    String[] driver_status={"SELECT", "Self Driven","Driver Inclusive"};
    String[] location={"Baringo County", "Bomet County", "Bungoma County", "Busia County", "Elgeyo Marakwet County", "Embu County"
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
    public static final String add_vehicle_url = "https://kenyanrides.com/android/write_vehicle.php";

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View myView = inflater.inflate(R.layout.fragment_sell, null);

       requestStoragePermission();

       alertDialogBuilder = new AlertDialog.Builder(getActivity());

       dialog = new ProgressDialog(getActivity());

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

       Button btnPostVehicle = myView.findViewById(R.id.btnPostVehicle);

       //edittext initialization
        editTextVehicleTitle =myView.findViewById(R.id.edit_text_vehicle_title);
        editTextVehicleOverview =myView.findViewById(R.id.edit_text_vehicle_overview);
        editTextPrice =myView.findViewById(R.id.edit_text_price);
        editTextModelYear =myView.findViewById(R.id.edit_text_model_year);

       //file chooser views
        TextView image1 = myView.findViewById(R.id.image1);
        TextView image2 = myView.findViewById(R.id.image2);
        TextView image3 = myView.findViewById(R.id.image3);
        TextView image4 = myView.findViewById(R.id.image4);
        TextView image5 = myView.findViewById(R.id.image5);

       image1FilePath = myView.findViewById(R.id.image1_file_path);
       image2FilePath = myView.findViewById(R.id.image2_file_path);
       image3FilePath = myView.findViewById(R.id.image3_file_path);
       image4FilePath = myView.findViewById(R.id.image4_file_path);
       image5FilePath = myView.findViewById(R.id.image5_file_path);


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
        final Spinner fuelSpinner = myView.findViewById(R.id.fuel_spinner);

        final Spinner seatsSpinner = myView.findViewById(R.id.seats_spinner);

        final Spinner driverSpinner = myView.findViewById(R.id.driver_status_spinner);

        final Spinner locationSpinner = myView.findViewById(R.id.location_spinner);

        brandSpinner = myView.findViewById(R.id.brand_spinner);


        checkBoxAirconditioner = myView.findViewById(R.id.chkbox_airconditioner);
        checkBoxPowerDoorLocks = myView.findViewById(R.id.chkbox_power_door_locks);
        checkBoxAntiLockBrakingSystem = myView.findViewById(R.id.chkbox_anti_lock_braking_system);
        checkBoxBrakeAssist = myView.findViewById(R.id.chkbox_brake_assist);
        checkBoxPowerSteering = myView.findViewById(R.id.chkbox_power_steering);
        checkBoxDriverAirbag = myView.findViewById(R.id.chkbox_driver_airbag);
        checkBoxPassengerAirbag = myView.findViewById(R.id.chkbox_passenger_airbag);
        checkBoxPowerWindows = myView.findViewById(R.id.chkbox_power_windows);
        checkBoxCDPlayer = myView.findViewById(R.id.chkbox_cdplayer);
        checkBoxCentralLocking = myView.findViewById(R.id.chkbox_central_locking);
        checkBoxCrashSensor = myView.findViewById(R.id.chkbox_crash_sensor);
        checkBoxLeatherSeats = myView.findViewById(R.id.chkbox_leather_seats);


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
        ArrayAdapter aa = new ArrayAdapter(getActivity(),R.layout.spinner_item,fuel);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        fuelSpinner.setAdapter(aa);

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
        ArrayAdapter seatsAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,seats);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        seatsSpinner.setAdapter(seatsAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object item = adapterView.getItemAtPosition(i);
                if (item != null) {

                    vehicleLocation = item.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter locationAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,location);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationSpinner.setAdapter(locationAdapter);

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
        ArrayAdapter driverAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,driver_status);
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        driverSpinner.setAdapter(driverAdapter);




        BackTask backTask =new BackTask();
        backTask.execute();

        btnPostVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postVehicle();
            }
        });




        return myView;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case IMAGE1:
                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image1FilePath.setText(getFileName(imageUri));

                image1Path = getPath(imageUri);



                break;

            case IMAGE2:

                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image2FilePath.setText(getFileName(imageUri));

                image2Path = getPath(imageUri);



                    break;

            case IMAGE3:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image3FilePath.setText(getFileName(imageUri));

                image3Path = getPath(imageUri);


                break;

            case IMAGE4:

               imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                image4FilePath.setText(getFileName(imageUri));

                image4Path = getPath(imageUri);

                break;

            case IMAGE5:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permission not Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(), "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getPath(Uri uri){
        String path = null;

        Cursor cursor =getActivity().getContentResolver().query(uri, null, null, null, null );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String document_id = cursor.getString(0);

        document_id =document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
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
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image2FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image3FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image4FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image5FilePath.getText().toString().equals("No file choosen")){
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        //check that spinner items are slected
        if (vehicleFuel.equals("SELECT")){
            Toast.makeText(getActivity(), "Please select vehicle fuel type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vehicleSeats.equals("SELECT")){
            Toast.makeText(getActivity(), "Please set number of seats in the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vehicleLocation.equals("SELECT")){
            Toast.makeText(getActivity(), "Please select location of the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }



        try{

            dialog.setMessage("Uploading vehicle...");
            dialog.setCancelable(false);
            dialog.show();

            user user = SharedPrefManager.getInstance(getActivity()).getUser();


            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(getActivity(), uploadId, add_vehicle_url)
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
                    .addParameter("ownerid", user.getEmail())
                    .addParameter("booked", "1")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            dialog.setMessage("Uploading vehicle...");
                            dialog.setCancelable(false);
                            dialog.show();


                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            dialog.dismiss();

                            alertDialogBuilder.setTitle("Failed!");
                            alertDialogBuilder.setMessage("Vehicle was not uploaded! Please try uploading again");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialogBuilder.show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            dialog.dismiss();

                            alertDialogBuilder.setTitle("Success!");
                            alertDialogBuilder.setMessage("Vehicle was uploaded succesfully");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //go to home fragment
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    HomeFragment llf = new HomeFragment();
                                    ft.replace(R.id.nav_host_fragment, llf);
                                    ft.commit();


                                }
                            });
                            alertDialogBuilder.show();

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                            Toast.makeText(context, String.valueOf(uploadInfo) , Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                            alertDialogBuilder.setTitle("Failed!");
                            alertDialogBuilder.setMessage("Vehicle was not uploaded! Please try uploading again");
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialogBuilder.show();

                        }
                    })
                    .startUpload();



        }catch (Exception e){

        }


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
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
        dialog = new ProgressDialog(getActivity());
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

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter brandAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,brandsList);
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
