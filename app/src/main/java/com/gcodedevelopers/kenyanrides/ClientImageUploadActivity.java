package com.gcodedevelopers.kenyanrides;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.IOException;
import java.util.UUID;

public class ClientImageUploadActivity extends AppCompatActivity {

    private TextView userImage, drivingLicense, nationalId, userImagePath,  drivingLicensePath,  nationalIdPath;

    private Button btnUpload;

    String imageString;
    String stringUserImagePath;

    String image2String;
    String stringDrivingLicensePath;

    String image3String;
    String stringNationalId;


    public static final int IMAGE1 = 1;
    public static final int IMAGE2 = 2;
    public static final int IMAGE3 = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;

    Uri imageUri;
    private Bitmap bitmap;

    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog dialog;

    public static final String add_vehicle_url = "https://kenyanrides.com/android/verification_images_upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_image_upload);

        requestStoragePermission();

        //initialize views
        userImage = findViewById(R.id.image1);
        drivingLicense = findViewById(R.id.image2);
        nationalId = findViewById(R.id.image3);

        btnUpload = findViewById(R.id.btnUpload);

        alertDialogBuilder = new AlertDialog.Builder(this);

        dialog = new ProgressDialog(this);

        userImagePath = findViewById(R.id.image1_file_path);
        drivingLicensePath = findViewById(R.id.image2_file_path);
        nationalIdPath = findViewById(R.id.image3_file_path);

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages();
            }
        });

        //file chooser click listener
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, IMAGE1);
            }
        });

        drivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image2Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image2Intent.setType("image/*");
                image2Intent = Intent.createChooser(image2Intent, "Choose a file");
                startActivityForResult(image2Intent, IMAGE2);

            }
        });

        nationalId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image3Intent = new Intent(Intent.ACTION_GET_CONTENT);
                image3Intent.setType("image/*");
                image3Intent = Intent.createChooser(image3Intent, "Choose a file");
                startActivityForResult(image3Intent, IMAGE3);

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
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                userImagePath.setText(getFileName(imageUri));

                stringUserImagePath = getPath(imageUri);


                break;

            case IMAGE2:

                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                drivingLicensePath.setText(getFileName(imageUri));

                stringDrivingLicensePath = getPath(imageUri);


                break;

            case IMAGE3:

                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                nationalIdPath.setText(getFileName(imageUri));

                stringNationalId = getPath(imageUri);


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

        Cursor cursor =this.getContentResolver().query(uri, null, null, null, null );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String document_id = cursor.getString(0);

        document_id =document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = this.getContentResolver().query(
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
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
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

    public void uploadImages(){

        //check if network is connected
        if (!isNetworkAvailable()){

            alertDialogBuilder.setTitle("Network Failure");
            alertDialogBuilder.setMessage("Please check your internet connection!");
            alertDialogBuilder.show();
            return;
        }

        //check that images are selected
        if (userImagePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (drivingLicensePath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nationalIdPath.getText().toString().equals("No file choosen")){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try{

            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();

            user user = SharedPrefManager.getInstance(this).getUser();
            int vehicle_id = getIntent().getIntExtra("vehicle_id", 0);


            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, uploadId, add_vehicle_url)
                    .addFileToUpload(stringUserImagePath, "user_image")
                    .addFileToUpload(stringDrivingLicensePath, "driving_license")
                    .addFileToUpload(stringNationalId, "national_id")
                    .addParameter("ownerid", user.getEmail())
                    .addParameter("vehicle_id", String.valueOf(vehicle_id))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            dialog.setMessage("Loading...");
                            dialog.setCancelable(false);
                            dialog.show();


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
}