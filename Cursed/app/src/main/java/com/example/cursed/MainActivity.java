package com.example.cursed;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment,new LoginFragment());
        ft.commit();
}
}