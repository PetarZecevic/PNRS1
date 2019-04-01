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

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private EditText mCity;
    private Button mSubmit;
    private AlertDialog mAlert;
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
        cityList.setAdapter(mAdapter);
        cityList.setOnItemClickListener(this);
        cityList.setOnItemLongClickListener(this);
        // Alert
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
        mAlert = builder1.create();
    }

    public void switchActivity(String cityName){
        Log.d(TAG, "City: " + cityName);
        Intent binder = new Intent(this, DetailsActivity.class);
        binder.putExtra(CITY_INDEX, cityName);
        startActivity(binder);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSubmit.getId()) {
            // Send city name to next activity.
            String cityName = String.valueOf(mCity.getText());
            if (!cityName.isEmpty())
                // Add city to list.
                mAdapter.addItem(new CityOption(cityName));
            else
                mAlert.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CityOption co = null;
        try{
            co = (CityOption)mAdapter.getItem(i);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }finally {
            Log.d(TAG, "Passed to new activity: " + co.getCityName());
            switchActivity(co.getCityName());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CityOption co = null;
        try{
            co = (CityOption) mAdapter.getItem(i);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            return false;
        }finally {
            Log.d(TAG, "Removing: " + co.getCityName());
            return mAdapter.removeItem(co);
        }
    }
}
