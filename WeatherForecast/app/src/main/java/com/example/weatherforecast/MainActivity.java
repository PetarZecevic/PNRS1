package com.example.weatherforecast;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText cityName;
    private Button displayCityStats;
    private AlertDialog cityAlert;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.city_name);
        displayCityStats = findViewById(R.id.display_stats);

        cityName.setOnClickListener(this);
        displayCityStats.setOnClickListener(this);

        createCityAlert();
    }

    private void createCityAlert()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.alert_city_text);
        builder1.setTitle(R.string.alert_city_title);
        builder1.setNeutralButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        cityAlert = builder1.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == displayCityStats.getId()) {
            // Send city name to next activity.
            String city = String.valueOf(cityName.getText());
            if (!city.isEmpty()) {
                Log.d(TAG, "City: " + city);
            } else {
                cityAlert.show();
            }
        }
    }
}
