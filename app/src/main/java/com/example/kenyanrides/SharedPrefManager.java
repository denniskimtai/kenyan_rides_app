package com.example.kenyanrides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "mysharedpref123";

    private static final String KEY_ID = "keyid";
    private static final String KEY_FIRST_NAME = "firstname";
    private static final String KEY_SECOND_NAME = "secondname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NATIONAL_ID = "id";
    private static final String KEY_MOBILE_NUMBER = "mobileumber";
    private static final String KEY_REG_DATE = "date";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences

    public void userLogin(user user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirst_name());
        editor.putString(KEY_SECOND_NAME, user.getSecond_name());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_NATIONAL_ID, user.getNational_id());
        editor.putString(KEY_MOBILE_NUMBER, user.getMobile_number());
        editor.putString(KEY_REG_DATE, user.getReg_date());

        editor.apply();
    }

    public Boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //check if there is email stored in shared pref
        if (sharedPreferences.getString(KEY_EMAIL, null ) != null) {
            //user is logged in
            return true;
        }
        //user is not logged in
        return false;

    }

    //this method will give the logged in user
    public user getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new user(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_NATIONAL_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_SECOND_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE_NUMBER, null),
                sharedPreferences.getString(KEY_REG_DATE, null)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //clear values saved in shared pref
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));

    }


}
