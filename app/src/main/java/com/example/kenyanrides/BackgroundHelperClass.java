package com.example.kenyanrides;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BackgroundHelperClass extends AsyncTask<String, Void, String> {

    Context context;

    private ProgressDialog dialog;
    private AlertDialog.Builder alertDialogBuilder;

    String hashed;

    BackgroundHelperClass(Context ctx) {

        context = ctx;
        alertDialogBuilder = new AlertDialog.Builder(context);


    }


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        String login_url = "https://kenyanrides.com/android/login.php";

        String register_url = "https://kenyanrides.com/android/registration.php";

        String update_user_data = "https://kenyanrides.com/android/update_user_data.php";

        String confirm_payment_url = "https://kenyanrides.com/android/verify_payment.php";

        String update_vehicle_url = "https://kenyanrides.com/android/update_vehicle.php";

        String delete_vehicle_url = "https://kenyanrides.com/android/delete_vehicle.php";


        //login
        if (type.equals("login")) {


            try {

                //variables
                String email = params[1];
                String password = params[2];

                hashed = MD5_Hash(password);

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(hashed, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("register")) {

            try {
                //variables
                String first_name = params[1];
                String second_name = params[2];
                String national_id = params[3];
                String phone_number = params[4];
                String email_address = params[5];
                String password = params[6];
                String reg_date = params[7];

                hashed = MD5_Hash(password);


                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" +
                        URLEncoder.encode("second_name", "UTF-8") + "=" + URLEncoder.encode(second_name, "UTF-8") + "&" +
                        URLEncoder.encode("national_id", "UTF-8") + "=" + URLEncoder.encode(national_id, "UTF-8") + "&" +
                        URLEncoder.encode("mobile_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8") + "&" +
                        URLEncoder.encode("email_address", "UTF-8") + "=" + URLEncoder.encode(email_address, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(hashed, "UTF-8") + "&" +
                        URLEncoder.encode("reg_date", "UTF-8") + "=" + URLEncoder.encode(reg_date, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (type.equals("update user details")){


            try {
                //variables
                String first_name = params[1];
                String second_name = params[2];
                String national_id = params[3];
                String phone_number = params[4];
                String email_address = params[5];
                String dob = params[6];
                String city = params[7];
                String country = params[8];
                String address = params[9];
                String user_id = params[10];


                URL url = new URL(update_user_data);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" +
                        URLEncoder.encode("second_name", "UTF-8") + "=" + URLEncoder.encode(second_name, "UTF-8") + "&" +
                        URLEncoder.encode("national_id", "UTF-8") + "=" + URLEncoder.encode(national_id, "UTF-8") + "&" +
                        URLEncoder.encode("mobile_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8") + "&" +
                        URLEncoder.encode("email_address", "UTF-8") + "=" + URLEncoder.encode(email_address, "UTF-8") + "&" +
                        URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dob, "UTF-8") + "&" +
                        URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8")+ "&" +
                        URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8")+ "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8")+ "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("payment")){

            try {

                //variables
                String pickupDate = params[1];
                String pickupTime = params[2];
                String vehicleTravelDestination = params[3];
                String returnDate = params[4];
                String returnTime = params[5];
                String phoneNumber = params[6];
                String pickupLocation = params[7];
                String returnLocation = params[8];
                String vehicle_id = params[9];
                String userEmail = params[10];
                String price_per_day = params[11];
                String vehicle_owner_email = params[12];
                String vehicle_title = params[13];

                URL url = new URL(confirm_payment_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("pickupDate", "UTF-8") + "=" + URLEncoder.encode(pickupDate, "UTF-8") + "&"
                        + URLEncoder.encode("pickupTime", "UTF-8") + "=" + URLEncoder.encode(pickupTime, "UTF-8") + "&"
                        + URLEncoder.encode("vehicleTravelDestination", "UTF-8") + "=" + URLEncoder.encode(vehicleTravelDestination, "UTF-8") + "&"
                        + URLEncoder.encode("returnDate", "UTF-8") + "=" + URLEncoder.encode(returnDate, "UTF-8") + "&"
                        + URLEncoder.encode("returnTime", "UTF-8") + "=" + URLEncoder.encode(returnTime, "UTF-8") + "&"
                        + URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&"
                        + URLEncoder.encode("pickupLocation", "UTF-8") + "=" + URLEncoder.encode(pickupLocation, "UTF-8") + "&"
                        + URLEncoder.encode("returnLocation", "UTF-8") + "=" + URLEncoder.encode(returnLocation, "UTF-8") + "&"
                        + URLEncoder.encode("vehicle_id", "UTF-8") + "=" + URLEncoder.encode(vehicle_id, "UTF-8") + "&"
                        + URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(userEmail, "UTF-8") + "&"
                        + URLEncoder.encode("price_per_day", "UTF-8") + "=" + URLEncoder.encode(price_per_day, "UTF-8") + "&"
                        + URLEncoder.encode("vehicle_owner_email", "UTF-8") + "=" + URLEncoder.encode(vehicle_owner_email, "UTF-8") + "&"
                        + URLEncoder.encode("vehicle_title", "UTF-8") + "=" + URLEncoder.encode(vehicle_title, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("update car details")){

            try {

                //variables
                String vehicle_title = params[1];
                String vehicle_brand = params[2];
                String vehicle_overview = params[3];
                String vehicle_price = params[4];
                String fuel = params[5];
                String vehicle_location = params[6];
                String vehicle_model_year = params[7];
                String vehicle_seats = params[8];
                String vehicle_driver_status = params[9];
                String airconditioner = params[10];
                String powerdoorlocks = params[11];
                String antilockbrakingsystem = params[12];
                String brakeAssist = params[13];
                String powerSteering = params[14];
                String driverAirbag = params[15];
                String passengerAirbag = params[16];
                String powerWindows = params[17];
                String cdPlayer = params[18];
                String centralLocking = params[19];
                String crashSensor = params[20];
                String leatherSeats = params[21];
                String vehicleId = params[22];
                String booked = params[23];

                URL url = new URL(update_vehicle_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("vehicle_title", "UTF-8") + "=" + URLEncoder.encode(vehicle_title, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle_brand", "UTF-8") + "=" + URLEncoder.encode(vehicle_brand, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle_overview", "UTF-8") + "=" + URLEncoder.encode(vehicle_overview, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle_price", "UTF-8") + "=" + URLEncoder.encode(vehicle_price, "UTF-8") + "&" +
                        URLEncoder.encode("fuel", "UTF-8") + "=" + URLEncoder.encode(fuel, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle_location", "UTF-8") + "=" + URLEncoder.encode(vehicle_location, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle_model_year", "UTF-8") + "=" + URLEncoder.encode(vehicle_model_year, "UTF-8")+ "&" +
                        URLEncoder.encode("vehicle_seats", "UTF-8") + "=" + URLEncoder.encode(vehicle_seats, "UTF-8")+ "&" +
                        URLEncoder.encode("vehicle_driver_status", "UTF-8") + "=" + URLEncoder.encode(vehicle_driver_status, "UTF-8") + "&" +
                        URLEncoder.encode("airconditioner", "UTF-8") + "=" + URLEncoder.encode(airconditioner, "UTF-8") + "&" +
                        URLEncoder.encode("powerdoorlocks", "UTF-8") + "=" + URLEncoder.encode(powerdoorlocks, "UTF-8") + "&" +
                        URLEncoder.encode("antilockbrakingsystem", "UTF-8") + "=" + URLEncoder.encode(antilockbrakingsystem, "UTF-8") + "&" +
                        URLEncoder.encode("brakeassist", "UTF-8") + "=" + URLEncoder.encode(brakeAssist, "UTF-8") + "&" +
                        URLEncoder.encode("powersteering", "UTF-8") + "=" + URLEncoder.encode(powerSteering, "UTF-8") + "&" +
                        URLEncoder.encode("driverairbag", "UTF-8") + "=" + URLEncoder.encode(driverAirbag, "UTF-8") + "&" +
                        URLEncoder.encode("passengerairbag", "UTF-8") + "=" + URLEncoder.encode(passengerAirbag, "UTF-8") + "&" +
                        URLEncoder.encode("powerwindows", "UTF-8") + "=" + URLEncoder.encode(powerWindows, "UTF-8") + "&" +
                        URLEncoder.encode("cdplayer", "UTF-8") + "=" + URLEncoder.encode(cdPlayer, "UTF-8") + "&" +
                        URLEncoder.encode("centrallocking", "UTF-8") + "=" + URLEncoder.encode(centralLocking, "UTF-8") + "&" +
                        URLEncoder.encode("crashsensor", "UTF-8") + "=" + URLEncoder.encode(crashSensor, "UTF-8") + "&" +
                        URLEncoder.encode("leatherseats", "UTF-8") + "=" + URLEncoder.encode(leatherSeats, "UTF-8") + "&" +
                        URLEncoder.encode("vehicleId", "UTF-8") + "=" + URLEncoder.encode(vehicleId, "UTF-8") + "&" +
                        URLEncoder.encode("booked", "UTF-8") + "=" + URLEncoder.encode(booked, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("delete vehicle")){

            try {

                //variables
                String vehicleId = params[1];

                URL url = new URL(delete_vehicle_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("vehicleId", "UTF-8") + "=" + URLEncoder.encode(vehicleId, "UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return result
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }





        return "";
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading, please wait...");
        dialog.setCancelable(false);
        dialog.show();


    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            try {
                if (dialog.isShowing()) {

                    dialog.dismiss();

                }
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                switch (result) {

                    case "User already registered":
                        alertDialogBuilder.setTitle("Registration Failed");
                        alertDialogBuilder.setMessage("The email already exists in the system ");
                        alertDialogBuilder.show();

                        break;


                    case "registration successful":
                        Intent regintent = new Intent(context, LoginActivity.class);
                        context.startActivity(regintent);
                        ((Activity) context).finish();

                        break;

                    case "registration failed":
                        alertDialogBuilder.setTitle("Registration Failed");
                        alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
                        alertDialogBuilder.show();

                        break;


                    case "Vehicle Added":
                        alertDialogBuilder.setMessage("Vehicle Added Successfully");
                        alertDialogBuilder.show();

                        break;


                    case "failed":
                        alertDialogBuilder.setTitle("Failed!");
                        alertDialogBuilder.setMessage("Vehicle not added. Please try again");
                        alertDialogBuilder.show();

                        break;

                    case "Record updated successfully":
                        alertDialogBuilder.setMessage("Record updated successfully");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        });
                        alertDialogBuilder.show();

                        break;

                    case "Error updating record":
                        alertDialogBuilder.setMessage("Update not successful. Please try again");
                        alertDialogBuilder.show();

                        break;

                    case "payment verified":

                        alertDialogBuilder.setTitle("Verified!");
                        alertDialogBuilder.setMessage("Payment is verified. Your booking request has been sent to the vehicle owner");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();

                            }
                        });
                        alertDialogBuilder.show();

                        break;

                    case "payment unverified":
                        alertDialogBuilder.setTitle("Failed!");
                        alertDialogBuilder.setMessage("We could not verify you payment. Please try again\nYou need to pay a 10% service fee ");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Try again", (dialogInterface, i) -> {


                        });
                        alertDialogBuilder.show();

                        break;

                    case "Encountered an error while sending:":
                        alertDialogBuilder.setMessage("sms error! Vehicle owner not notified, please contact them directly");
                        alertDialogBuilder.show();

                        break;

                    case "Vehicle updated successfully":
                        dialog.dismiss();

                        alertDialogBuilder.setTitle("Success!");
                        alertDialogBuilder.setMessage("Vehicle updated successfully");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();

                            }
                        });
                        alertDialogBuilder.show();

                        break;

                    case "Vehicle failed to update":
                        alertDialogBuilder.setTitle("Failed!");
                        alertDialogBuilder.setMessage("Vehicle not updated! Please try again");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Try again", (dialogInterface, i) -> {


                        });
                        alertDialogBuilder.show();

                        break;

                    case "Vehicle deleted successfully":
                        alertDialogBuilder.setMessage("Vehicle deleted");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {

                            //go to main activity
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                        });
                        alertDialogBuilder.show();

                        break;

                    case "Vehicle failed to delete":
                        alertDialogBuilder.setTitle("Failed!");
                        alertDialogBuilder.setMessage("Vehicle not deleted! Please try again");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Try again", (dialogInterface, i) -> {

                        });
                        alertDialogBuilder.show();

                        break;

                    default:
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(result);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                user user = new user(
                                        userJson.getInt("id"),
                                        userJson.getInt("national_id"),
                                        userJson.getString("first_name"),
                                        userJson.getString("second_name"),
                                        userJson.getString("email"),
                                        userJson.getString("mobile_number"),
                                        userJson.getString("reg_date"),
                                        userJson.getString("dob"),
                                        userJson.getString("Address"),
                                        userJson.getString("city"),
                                        userJson.getString("country")

                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(context).userLogin(user);

                                //start main activity
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            } else {

                                alertDialogBuilder.setTitle("Login Failed");
                                alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
                                alertDialogBuilder.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            } catch (Exception ex) {

                alertDialogBuilder.setMessage("System failed fetching details! Please try again");
                alertDialogBuilder.show();

            }

        } else {
            Toast.makeText(context, "Error iko hapa", Toast.LENGTH_SHORT).show();
        }

        }


        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }


        public static String MD5_Hash (String s){
            MessageDigest m = null;

            try {
                m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            m.update(s.getBytes(), 0, s.length());
            String hash = new BigInteger(1, m.digest()).toString(16);
            return hash;
        }

    }













