package com.example.cursed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.apache.commons.math3.util.Precision;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class currency_info extends Fragment {
    TextView nameTV, symbolTV, valueTV, maxTV, minTV, changeTV;
    SharedPreferences sharedPref;
    LineChartView lineChartView;
    String lastData;
    ScheduledExecutorService scheduler;
    ArrayList<Float> values;
    ArrayList<String> date;
    ArrayList<String> roundedValues;
    Resources resources;
    Handler handler;
    Float min, max, change;
    LinearLayout mainLayout;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view3 = inflater.inflate(R.layout.fragment_currency_info, container, false);
        values = new ArrayList<>();
        date = new ArrayList<>();
        roundedValues = new ArrayList<>();
        mainLayout = view3.findViewById(R.id.mainLayout);
        progressBar = view3.findViewById(R.id.progress_loader);
        lineChartView = view3.findViewById(R.id.chart);
        nameTV = view3.findViewById(R.id.nameTV);
        symbolTV = view3.findViewById(R.id.symbolTV);
        valueTV = view3.findViewById(R.id.valueTV);
        maxTV = view3.findViewById(R.id.maxTV);
        minTV = view3.findViewById(R.id.minTV);
        changeTV = view3.findViewById(R.id.changeTV);
        sharedPref = getActivity().getSharedPreferences("currName", Context.MODE_PRIVATE);
        resources = getResources();
        handler = new Handler();

        // Inflate the layout for this fragment
        return view3;
    }

    @Override
    public void onResume() {
        super.onResume();
        scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new loadData(), 1, 3, TimeUnit.SECONDS);

    }

    @Override
    public void onDestroy() {
        scheduler.shutdown();
        super.onDestroy();
    }

    public class loadData implements Runnable {
        @Override
        public void run() {
            Float currentValue = Float.valueOf(sharedPref.getString("currency_value", null));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    valueTV.setText(String.valueOf(Precision.round(currentValue, 3)));
                    nameTV.setText(sharedPref.getString("currency_name", null));
                    symbolTV.setText(sharedPref.getString("currency_symbol", null));
                    if (values.size() >= 2) {
                        try {
                            min = Collections.min(values);
                            max = Collections.max(values);
                            maxTV.setText(max.toString());
                            minTV.setText(min.toString());
                            change = max - min;
                            changeTV.setText(change.toString());
                            progressBar.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);
                        } catch (NullPointerException e) {
                            Log.d("ERR", "FATAL");
                        }
                    }
                }
            });

            if (!values.contains(currentValue)) {
                values.add(currentValue);
                date.add(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                roundedValues.add(String.valueOf(Precision.round(currentValue, 3)));
                List<PointValue> chartValuesY = new ArrayList<>();
                List<AxisValue> axisValuesForX = new ArrayList<>();
                List<AxisValue> axisValuesForY = new ArrayList<>();
                for (int i = 0; i < values.size(); i++) {
                    chartValuesY.add(new PointValue(i, values.get(i)));
                    axisValuesForX.add(new AxisValue(i).setLabel(date.get(i)));
                    axisValuesForY.add(new AxisValue(values.get(i)).setLabel(roundedValues.get(i)));
                }
                Axis axeX = new Axis(axisValuesForX);
                Axis axeY = new Axis(axisValuesForY);
                LineChartData data = new LineChartData();
                List<Line> lines = new ArrayList();
                lines.add(new Line(chartValuesY).setColor(resources.getColor(R.color.colorPrimary)).setCubic(false));
                data.setLines(lines);
                axeY.setMaxLabelChars(8);
                axeX.setTextColor(resources.getColor(R.color.colorPrimary));
                axeY.setTextColor(resources.getColor(R.color.colorPrimary));
                data.setAxisXBottom(axeX);
                data.setAxisYLeft(axeY);
                lineChartView.setLineChartData(data);
            }

        }
    }
}