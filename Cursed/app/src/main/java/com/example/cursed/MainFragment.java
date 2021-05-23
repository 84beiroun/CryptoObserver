package com.example.cursed;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;




public class MainFragment extends Fragment implements AsyncResponse{
    String trueResult = null;
    TextView tvo;
    SharedPreferences sharedPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view2 = inflater.inflate(R.layout.fragment_main, container, false);
        tvo = view2.findViewById(R.id.outputTV);
        sharedPref = getActivity().getSharedPreferences("currName", Context.MODE_PRIVATE);
        String lastData = sharedPref.getString("lastCurr", null);
        try{
            tvo.setText("Last Service data: " + lastData);
        }
        catch (NullPointerException e) {
            tvo.setText("Just push the button");
        }
        Button getButton = view2.findViewById(R.id.getButton);
        final String url = "https://api.coincap.io/v2/assets/";
        final EditText inputET = view2.findViewById(R.id.inputET);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString("requestCursed", url + inputET.getText());
    editor.apply();

    ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate
            (new netRequest(), 0, 15, TimeUnit.SECONDS);

    getActivity().startService(new Intent(getActivity(), OnStartService.class));

            }
        });


        return view2;
    }

    @Override
    public void processFinish(String output) {
     tvo.setText(output);
    }
    public class netRequest implements Runnable
    {
        @Override
        public void run() {
            NetworkTask nT = new NetworkTask();
            nT.fullUrl = sharedPref.getString("requestCursed","DEFAULT");
            nT.delegate = MainFragment.this;
            nT.execute();
        }
    }
}