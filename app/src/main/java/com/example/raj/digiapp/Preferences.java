package com.example.raj.digiapp;

import android.app.Activity;
import android.content.SharedPreferences;

public class Preferences extends Activity {
    //Get login details and save to shared preferences.
    SharedPreferences pref = getApplicationContext().getSharedPreferences("DigiApp", 0);
    SharedPreferences.Editor editor = pref.edit();

    public void inPref(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public String outPref (String key){
        return pref.getString(key, "0");
    }
}


