package com.example.raj.digiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DBConnection extends AsyncTask<String, Void, Map<String,String>> {

    //Shared Preferences Class
    Preferences pref;

    AlertDialog alertDialog;

    public Map<String,String> result =new HashMap<>();

    Context context;

    //Set context to Application Context
    public DBConnection(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * This is the doInBG method. It takes multiple arguments as parameter. So always send 'type' as the first parameter.
     * @param params
     * @return
     */
    @Override
    protected Map<String, String> doInBackground(String... params) {

        //Get type of operation.
        String type = params[0];

        if (type.equals("login")) {
            verifyLoginCred();
            return result;
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(Map<String,String> result) {
        context.startActivity(new Intent(context, DigiAppLogin.class));
    }

    /* Not sure if we need it now.
    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(context).create();
        alertDialog.setTitle("LoginStatus");
    }
    */

    public void verifyLoginCred(){
        // Get login details from shared preferences.
        String empID = pref.outPref("empID");
        String pwd = pref.outPref("pass");

        //Create Query URL.
        String parameters = "empID=" + empID + "&pwd=" + pwd;
        String baseUrl = "http://digiappcom.000webhostapp.com/login.php?";
        String finalURL = baseUrl + parameters;

        //Hit DB to get validation.
        String line = "";
        String lineString="";
        try {
            URL url = new URL(finalURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            InputStream inputStreamReader = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader, "iso-8859-1"));
                while ((line = bufferedReader.readLine()) != null) {
                    lineString += line;
                }
                String[] response=lineString.split("&");
                if(response[0].equals("connection ok")){
                    pref.inPref("login_stat", "OK");
                    result.put("empId",response[1]);
                    result.put("empName",response[2]);
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
