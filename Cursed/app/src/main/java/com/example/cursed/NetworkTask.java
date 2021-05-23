package com.example.cursed;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkTask extends AsyncTask<String, Void, String> {
        public AsyncResponse delegate = null;
        public String fullUrl;
    @Override
    protected String doInBackground(String... string) {
        String value = null;
        String result;
        URL trueUrl;
        try {
            trueUrl = new URL(fullUrl);
            HttpsURLConnection conn = (HttpsURLConnection) trueUrl.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            value= br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    protected void onPostExecute(String result) {
String name = null, symbol = null, price = null, trueResult = null;

        try {
           JSONObject json = new JSONObject(result);
           name = json.getJSONObject("data").getString("name");
           symbol = json.getJSONObject("data").getString("symbol");
            price = json.getJSONObject("data").getString("priceUsd");
            trueResult = name + " " + symbol + " " + price;
            delegate.processFinish(trueResult);
        } catch (Exception e) {
            delegate.processFinish("Wrong cryptocurrency name");
        }


    }
}
