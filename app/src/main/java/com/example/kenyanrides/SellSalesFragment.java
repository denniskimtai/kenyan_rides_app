package com.example.kenyanrides;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SellSalesFragment extends Fragment {

    private String brands_url = "https://kenyanrides.com/android/fetch_brands.php";

    private List<String> brandsList = new ArrayList<>();
    private List<Integer> brandsId = new ArrayList<Integer>();

    private Bitmap bitmap;
    private String imageString;
    private String image1Path;

    private String image2String;
    private String image2Path;

    private String image3String;
    private String image3Path;

    String image4String;
    private String image4Path;

    String image5String;
    private String image5Path;


    private String[] fuel={"SELECT", "Petrol","Diesel","Hybrid"};

    private String[] seats={"SELECT", "2 Seater", "4 Seater", "5 Seater", "7 Seater", "8 Seater",
            "9 Seater", "10 Seater", "11 Seater", "12 Seater", "13 Seater", "14 Seater",
            "18 Seater", "24 Seater", "32 Seater", "40 Seater", "56 Seater"};

    public static final int IMAGE = 1;
    public static final int CAMERA = 2;
    public static final int VIDEO = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    public static final String vehicle_on_sale_url = "https://kenyanrides.com/android/write_vehicle_on_sale.php";

    private EditText editTextVehicleOverview, editTextVehicleTitle, editTextPrice, editTextModelYear;

    private Uri imageUri;
    private Uri videoUri = null;
    String imagePath;
    private Spinner brandSpinner;

    private String videoPath = "";

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog dialog;

    //upload strings
    private String vehicleBrand;
    private String vehicleFuel;
    private String vehicleSeats;
    private String vehicleLocation = "";

    private String airConditioner;
    private String powerDoorLocks;
    private String antiLockBrakingSystem;
    private String brakeAssist;
    private String powerSteering;
    private String driverAirBag;
    private String passengerAirBag;
    private String powerWindows;
    private String cdPlayer;
    private String centralLocking;
    private String crashSensor;
    private String leatherSeats;

    private CheckBox checkBoxAirconditioner;
    private CheckBox checkBoxPowerDoorLocks;
    private CheckBox checkBoxAntiLockBrakingSystem;
    private CheckBox checkBoxBrakeAssist;
    private CheckBox checkBoxPowerSteering;
    private CheckBox checkBoxDriverAirbag;
    private CheckBox checkBoxPassengerAirbag;
    private CheckBox checkBoxPowerWindows;
    private CheckBox checkBoxCDPlayer;
    private CheckBox checkBoxCentralLocking;
    private CheckBox checkBoxCrashSensor;
    private CheckBox checkBoxLeatherSeats;

    private RecyclerView recyclerView;
    private HorizontalCarImagesAdapter horizontalCarImagesAdapter;
    private ArrayList<Uri> uri = new ArrayList<>();

    private int selectedImages = 0;

    private TextView text_view_remaining_images;

    private ImageView addVideo;
    private VideoView videoView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_sell_sales, null);

        requestStoragePermission();
        requestCameraPermission();

        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        dialog = new ProgressDialog(getActivity());

        //multipart upload namespace
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        Button btnPostVehicle = myView.findViewById(R.id.btnPostVehicleForSale);

        //edittext initialization
        editTextVehicleTitle =myView.findViewById(R.id.edit_text_sale_vehicle_title);
        editTextVehicleOverview =myView.findViewById(R.id.edit_text_sale_vehicle_overview);
        editTextPrice =myView.findViewById(R.id.edit_text_sale_price);
        editTextModelYear =myView.findViewById(R.id.edit_text_sale_model_year);

        text_view_remaining_images = myView.findViewById(R.id.text_view_remaining_images);


        //horizontal recycler view initialization
        recyclerView = myView.findViewById(R.id.carImagesRecyclerview);
        horizontalCarImagesAdapter = new HorizontalCarImagesAdapter(getActivity(),uri);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(horizontalCarImagesAdapter);

        //file chooser views
        ImageView imageChooser = myView.findViewById(R.id.image1);
        addVideo = myView.findViewById(R.id.add_video);

        videoView = myView.findViewById(R.id.video_view);


        //file chooser click listener
        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if user has already selected 5 images
                if (selectedImages == 5){
                    alertDialogBuilder.setMessage("You can only select 5 vehicle images");
                    alertDialogBuilder.show();

                }else {

                    selectImage(getActivity());
                }


            }
        });

        //add video click listener
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectVideo(getActivity());

            }
        });


        //spinner initialization
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner fuelSpinner = myView.findViewById(R.id.fuel_spinner);

        final Spinner seatsSpinner = myView.findViewById(R.id.seats_spinner);

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

        //places autocomplete
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setCountry("KE");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocompleteFragment.setHint("SELECT");
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setVisibility(View.GONE);

        //customise autocomplete edittext
        EditText etPlace = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        etPlace.setTextSize(12.0f);
        etPlace.setHintTextColor(getActivity().getResources().getColor(R.color.black));
        etPlace.setGravity(Gravity.CENTER_VERTICAL);
        etPlace.setBackground(getActivity().getResources().getDrawable(R.drawable.input_shape));
        etPlace.setBackgroundColor(getActivity().getResources().getColor(R.color.grey));
        etPlace.setPadding(15,25,15,60);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                vehicleLocation = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getActivity().getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

        if(resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case CAMERA:

                    if (resultCode == RESULT_OK && data != null) {

                        selectedImages ++;
                        int remaining_images = 5 - selectedImages;
                        text_view_remaining_images.setText("You can add " + remaining_images + "more images");

                        imageUri = data.getData();

                        uri.add(imageUri);

                        recyclerView.setVisibility(View.VISIBLE);
                        horizontalCarImagesAdapter.notifyDataSetChanged();

                    }
                    break;

                case IMAGE:

                    if (resultCode == RESULT_OK && data != null) {

                        if(data.getClipData() == null){

                            selectedImages++;
                            int remaining_images = 5 - selectedImages;
                            text_view_remaining_images.setText("You can add " + remaining_images + "more images");
                            //only one image is selected
                            imageUri = data.getData();

                            uri.add(imageUri);

                            recyclerView.setVisibility(View.VISIBLE);
                            horizontalCarImagesAdapter.notifyDataSetChanged();

                        } else{
                            //several images are selected

                            int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.

                            //check number of images selected
                            if (count > 5 || selectedImages + count > 5){

                                alertDialogBuilder.setMessage("You can only select a maximum of 5 images");
                                alertDialogBuilder.show();
                                return;

                            }else {

                                //selected images are 5 or less

                                for (int i = 0; i < count; i++){

                                    selectedImages++;

                                    imageUri = data.getClipData().getItemAt(i).getUri();

                                    uri.add(imageUri);

                                    recyclerView.setVisibility(View.VISIBLE);
                                    horizontalCarImagesAdapter.notifyDataSetChanged();
                                }
                                int remaining_images = 5 - selectedImages;
                                text_view_remaining_images.setText("You can add " + remaining_images + " more images");
                            }
                        }
                    }
                    break;

                case VIDEO:

                    if (resultCode == RESULT_OK && data != null) {

                        videoUri = data.getData();

                        addVideo.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoURI(videoUri);
                        videoView.start();

                        videoPath = getVideoPath(videoUri);

                    }
                    break;



            }
        }else {
            Toast.makeText(getActivity(), "No images selected", Toast.LENGTH_SHORT).show();
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

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(), "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 101){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Camera permission Granted", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(), "Camera permission not Granted", Toast.LENGTH_SHORT).show();
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

    public String getVideoPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }


    //select image dialog method
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Vehicle Images");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERA);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent chooseFile = new Intent();
                chooseFile.setType("image/*");
                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, IMAGE);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //select video dialog method
    private void selectVideo(Context context) {
        final CharSequence[] options = { "Use Camera", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Car Video");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Use Camera")) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, VIDEO);
                }

            } else if (options[item].equals("Choose from Gallery")) {
                Intent chooseFile = new Intent();
                chooseFile.setType("video/*");
                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, VIDEO);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
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

        //check if user has selected 5 images

        if (selectedImages != 5){
            Toast.makeText(getActivity(), "Please select 5 vehicle images to continue", Toast.LENGTH_SHORT).show();
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

        if (vehicleLocation.isEmpty()){
            Toast.makeText(getActivity(), "Please select location of the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vehicleBrand.equals("SELECT")){
            Toast.makeText(getActivity(), "Please select the brand of the vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        //get path of images
        image1Path = getPath(uri.get(0));
        image2Path = getPath(uri.get(1));
        image3Path = getPath(uri.get(2));
        image4Path = getPath(uri.get(3));
        image5Path = getPath(uri.get(4));

        user user = SharedPrefManager.getInstance(getActivity()).getUser();

        if (videoPath.isEmpty()){

            try{

                String uploadId = UUID.randomUUID().toString();

                new MultipartUploadRequest(Objects.requireNonNull(getActivity()), uploadId,
                        vehicle_on_sale_url)
                        .addFileToUpload(image1Path, "image1")
                        .addFileToUpload(image2Path, "image2")
                        .addFileToUpload(image3Path, "image3")
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
                        .addParameter("booked", "10")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                dialog.setMessage("Uploading vehicle.\nPlease wait...");
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
                                alertDialogBuilder.setMessage("Vehicle was uploaded successfully");
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //go to main activity
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        getActivity().finish();
                                        startActivity(intent);

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

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        } else {

            try{

                String uploadId = UUID.randomUUID().toString();

                new MultipartUploadRequest(Objects.requireNonNull(getActivity()), uploadId,
                        vehicle_on_sale_url)
                        .addFileToUpload(image1Path, "image1")
                        .addFileToUpload(image2Path, "image2")
                        .addFileToUpload(image3Path, "image3")
                        .addFileToUpload(image4Path, "image4")
                        .addFileToUpload(image5Path, "image5")
                        .addFileToUpload(videoPath, "car_video")
                        .addParameter("vehicle_title", vehicleTitle)
                        .addParameter("vehicle_brand", vehicleBrand)
                        .addParameter("vehicle_overview", vehicleOverview)
                        .addParameter("vehicle_price", vehiclePrice)
                        .addParameter("fuel", vehicleFuel)
                        .addParameter("vehicle_location", vehicleLocation)
                        .addParameter("vehicle_model_year", vehicleModelYear)
                        .addParameter("vehicle_seats", vehicleSeats)
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
                        .addParameter("booked", "10")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                dialog.setMessage("Uploading vehicle.\nPlease wait...");
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
                                alertDialogBuilder.setMessage("Vehicle was uploaded successfully");
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //go to main activity
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        getActivity().finish();
                                        startActivity(intent);

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

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

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

            brandsList.add(0, "SELECT");
            brandsId.add(0, 0);

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

                    vehicleBrand = "SELECT";

                }
            });



        }
    }


}
