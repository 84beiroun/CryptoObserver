package com.example.cursed;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            value = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    protected void onPostExecute(String result) {
        String name = null, symbol = null, price = null, trueResult = null;
        int code = 0;
        try {
            JSONObject json = new JSONObject(result);
            name = json.getJSONObject("data").getString("name");
            symbol = json.getJSONObject("data").getString("symbol");
            price = json.getJSONObject("data").getString("priceUsd");
            trueResult = name + " " + symbol + " " + price;
            delegate.processFinish(trueResult, name, symbol, Float.valueOf(price), code);
        } catch (Exception e) {
            code = 1;
            delegate.processFinish("Wrong cryptocurrency name", "err", "err", 0, code);
        }


    }
}
