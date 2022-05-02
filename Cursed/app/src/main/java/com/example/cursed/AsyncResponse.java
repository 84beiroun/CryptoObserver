package com.example.cursed;

public interface AsyncResponse {
    void processFinish(String output, String name, String symbol, float value, int code);
    String fullUrl = null;
}
