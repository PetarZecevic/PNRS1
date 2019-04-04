package com.example.weatherforecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText mCity;
    private Button mSubmit;
    private AlertDialog mAlert1, mAlert2;
    private CityAdapter mAdapter;
    private static final String TAG = "MainActivity";
    public static final String CITY_INDEX = "city_name_index";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add city
        mCity = findViewById(R.id.city_name);
        mSubmit = findViewById(R.id.input_button);
        mSubmit.setOnClickListener(this);
        // Cities list
        ListView cityList = findViewById(R.id.city_list);
        mAdapter = new CityAdapter(this);
        cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityList.setAdapter(mAdapter);
        // Alert
        mAlert1 = createAlert("Prazno ime grada");
        mAlert2 = createAlert("Grad je već unet u listu");
    }

    private AlertDialog createAlert(String alertText)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(alertText);
        builder1.setTitle("Greška");
        builder1.setNeutralButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        return builder1.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSubmit.getId()) {
            // Send city name to next activity.
            String cityName = String.valueOf(mCity.getText());
            if (!cityName.isEmpty()) {
                CityOption c = new CityOption(cityName);
                if(!mAdapter.hasItem(c))
                    // Add city to list.
                    mAdapter.addItem(c);
                else
                    mAlert2.show();
            }
            else
                mAlert1.show();
        }
    }
}
