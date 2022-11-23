package com.example.cursed;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainFragment extends Fragment implements AsyncResponse {
    TextView tvo;
    SharedPreferences sharedPref;
    ListView listView;
    View view2;
    String lastData, url;
    EditText inputET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view2 = inflater.inflate(R.layout.fragment_main, container, false);
        tvo = view2.findViewById(R.id.outputTV);
        sharedPref = getActivity().getSharedPreferences("currName", Context.MODE_PRIVATE);
        listView = view2.findViewById(R.id.listView);
        lastData = sharedPref.getString("lastCurr", null);
        try {
            tvo.setText("Last Service data: " + lastData);
        } catch (NullPointerException e) {
            tvo.setText("Just push the button");
        }
        Button getButton = view2.findViewById(R.id.getButton);
        url = "https://api.coincap.io/v2/assets/";
        inputET = view2.findViewById(R.id.inputET);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view2.getContext(), R.array.cryptocurrencies, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                startTask(url + ((TextView) itemClicked).getText());
            }
        });
        inputET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                RequestHttps requestHttps = new RequestHttps();
                if (inputET.length() > 3) {
                    if (requestHttps.doRequest(url + inputET.getText()) == null) {
                        inputET.setTextColor(Color.RED);
                        getButton.setEnabled(false);
                    } else {
                        inputET.setTextColor(Color.GREEN);
                        getButton.setEnabled(true);
                    }
                } else {
                    inputET.setTextColor(Color.RED);
                    getButton.setEnabled(false);
                }
            }
        });
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                startTask(url + inputET.getText());
            }
        });
        return view2;
    }

    public void startTask(String currency) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("requestCursed", currency);
        editor.apply();
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new netRequest(), 0, 5, TimeUnit.SECONDS);
        getActivity().startService(new Intent(getActivity(), OnStartService.class));
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragment, new currency_info(), null).addToBackStack(null).commit();

    }

    @Override
    public void processFinish(String output, String name, String symbol, float value, int code) {
        try {
            if (!Objects.equals(sharedPref.getString("lastCurr", null), output)) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("currency_name", name);
                editor.putString("currency_symbol", symbol);
                editor.putString("currency_value", String.valueOf(value));
                editor.apply();
            }
        } catch (NullPointerException e) {
            Log.e("ERR1", "NullPointerAtProcessFinish");
        }
    }

    public class netRequest implements Runnable {
        @Override
        public void run() {
            NetworkTask nT = new NetworkTask();
            nT.fullUrl = sharedPref.getString("requestCursed", "DEFAULT");
            nT.delegate = MainFragment.this;
            nT.execute();
        }
    }
}