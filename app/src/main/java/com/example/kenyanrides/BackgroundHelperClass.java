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
                                        userJson.getString("reg_date")

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

                alertDialogBuilder.setMessage("System failed fetching vehciles! Please try again");
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













