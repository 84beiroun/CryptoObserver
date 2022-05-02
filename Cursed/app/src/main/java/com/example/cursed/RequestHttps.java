package com.example.cursed;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RequestHttps {
    String fullName;
    String price;
    JSONObject json;
    String doRequest(String URL) {
        URL trueUrl;
        String value = "";
        try {
            trueUrl = new URL(URL);
            HttpsURLConnection conn = (HttpsURLConnection) trueUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            value= br.readLine();
            br.close();
            try {
                json = new JSONObject(value);
                fullName = json.getJSONObject("data").getString("name") + " " + json.getJSONObject("data").getString("symbol");
                price = json.getJSONObject("data").getString("priceUsd");
            } catch (Exception e) {
               e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullName;
    }
}
