package com.example.kenyanrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BackgroungHelperClass extends AsyncTask<String, Void, String> {

    Context context;

    private ProgressDialog dialog;
    private AlertDialog.Builder alertDialogBuilder;

    String hashed;

    BackgroungHelperClass(Context ctx){

        context = ctx;
        alertDialogBuilder = new AlertDialog.Builder(context);


    }


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        String login_url = "https://kenyanrides.com/android/login.php";

        String register_url = "https://kenyanrides.com/android/registration.php";

        //login
        if (type.equals("login")){


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

                String postData = URLEncoder.encode("email","UTF-8") + "="+ URLEncoder.encode(email,"UTF-8") + "&"
                        + URLEncoder.encode("password","UTF-8") + "="+ URLEncoder.encode(hashed,"UTF-8") ;

                    bufferedWriter.write(postData);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine() )!= null){

                    result += line ;

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

        else if (type.equals("register")){

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

                String postData = URLEncoder.encode("first_name","UTF-8") + "="+ URLEncoder.encode(first_name,"UTF-8") + "&" +
                        URLEncoder.encode("second_name","UTF-8") + "="+ URLEncoder.encode(second_name,"UTF-8") + "&" +
                        URLEncoder.encode("national_id","UTF-8") + "="+ URLEncoder.encode(national_id,"UTF-8") + "&" +
                        URLEncoder.encode("mobile_number","UTF-8") + "="+ URLEncoder.encode(phone_number,"UTF-8") + "&" +
                        URLEncoder.encode("email_address","UTF-8") + "="+ URLEncoder.encode(email_address,"UTF-8") + "&" +
                        URLEncoder.encode("password","UTF-8") + "="+ URLEncoder.encode(hashed,"UTF-8") + "&" +
                        URLEncoder.encode("reg_date","UTF-8") + "="+ URLEncoder.encode(reg_date,"UTF-8") ;

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //read post request
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine() )!= null){

                    result += line ;

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


        return null;
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

        if (dialog.isShowing()) {

            dialog.dismiss();

        }

        if (result.equals("Login successful") ){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

        }

        if (result.equals("Login not successful")) {

            alertDialogBuilder.setTitle("Login Failed");
            alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
            alertDialogBuilder.show();

        }

            //if registration is successful
        if (result.equals("registration successful") ){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

        } else {

            alertDialogBuilder.setTitle("Registration Failed");
            alertDialogBuilder.setMessage("Please try again! \nEnsure you have internet connection and your credentials are correct");
            alertDialogBuilder.show();

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    public static String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

}


