package com.example.weatherforecast;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemLongClickListener
{
    private static final String LOG_TAG = "MainActivity";
    public static final String CITY_INDEX = "city_name_index";
    private EditText mCity;
    private Button mSubmit;
    private AlertDialog mAlert1, mAlert2;
    private CityAdapter mAdapter;

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
        cityList.setOnItemLongClickListener(this);
        // Alert
        mAlert1 = createAlert("Prazno ime grada", this);
        mAlert2 = createAlert("Grad je već unet u listu", this);
    }

    public static AlertDialog createAlert(String alertText, Context context)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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
            cityName = cityName.trim();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CityOption co = null;
        try {
            co = (CityOption) mAdapter.getItem(i);
            return mAdapter.removeItem(co);
        }catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
