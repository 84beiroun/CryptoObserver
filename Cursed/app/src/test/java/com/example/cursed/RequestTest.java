package com.example.cursed;


import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {
    RequestHttps requestHttps;
    String URL = "https://api.coincap.io/v2/assets/bitcoin";
    @BeforeEach
    void setUp(){
        requestHttps = new RequestHttps();
        requestHttps.json = new JSONObject();
        requestHttps.doRequest(URL);
    }
    @Test
    public void sendRequest() {
        assertEquals("Bitcoin BTC", requestHttps.fullName);
    }
    @Test
    public void priceRequest(){
        assertNotNull(requestHttps.price);
    }
}
