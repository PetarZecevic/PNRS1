package com.example.weatherforecast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, onUpdateListener{
    public final static String LOG_TAG = "DetailsActivity";
    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final static String IMG_URL = "http://openweathermap.org/img/w/";
    private final static String METRIC = "units=metric";
    private final static String APPID = "APPID=43316f9684dd3d0af930ecbb1b39fb35";
    private Calendar mCalendar;
    private TextView mLocation, mDay;
    private Button mTemperatureOption, mSunriseOption, mWindOption;
    private Display mWindDisplay, mSunriseDisplay;
    private Locale mLocal;
    private Timer mTimer;
    private WeatherTimerTask mRefresher;
    private WeatherData mWeatherData;

    private class TemperatureDisplay extends Display{
        public ArrayAdapter<String> unitsAdapter;
        public String scaleSelected;
    }
    private TemperatureDisplay mTemperatureDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Set calendar.
        mCalendar = Calendar.getInstance();
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        // Get data sent from Main activity.
        String locationName = getIntent().getStringExtra(MainActivity.CITY_INDEX);
        mLocation = findViewById(R.id.location);
        mDay = findViewById(R.id.day);
        mLocation.setText((CharSequence)locationName);

        mLocal = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        mWeatherData = new WeatherData();
        setDay();
        initDisplays();
        initOptions();
        setBackgroundRefresh();
    }

    private void setDay()
    {
        String day = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, mLocal);
        day = day.substring(0,1).toUpperCase(mLocal) + day.substring(1);
        mDay.setText(day);
    }

    private void setBackgroundRefresh()
    {
        mTimer = new Timer();
        mRefresher = new WeatherTimerTask(BASE_URL+mLocation.getText()+"&"+METRIC+"&"+APPID ,this, this);
        mTimer.schedule(mRefresher, 0, 5000L);
    }

    @Override
    public void update(JSONObject jsonObject) throws JSONException {
        Log.d(LOG_TAG, "JSON: " + jsonObject.toString());
        JSONObject sysObj = jsonObject.getJSONObject("sys"); // sunset.
        JSONObject mainObj = jsonObject.getJSONObject("main"); // temperature, humidity, pressure.
        JSONObject windObj = jsonObject.getJSONObject("wind");

        mWeatherData.temperature = mainObj.getDouble("temp");
        mWeatherData.humidity = mainObj.getDouble("humidity");
        mWeatherData.pressure = mainObj.getDouble("pressure");
        // Convert unix timestamp to clock format.
        long hour, minute;
        long sunrise = sysObj.getLong("sunrise");
        long sunset = sysObj.getLong("sunset");
        minute = TimeUnit.MILLISECONDS.toMinutes(sunrise);
        hour = TimeUnit.MILLISECONDS.toHours(sunrise);
        mWeatherData.sunUp = String.format(mLocal,"%02d:%02d", hour, minute);
        minute = TimeUnit.MILLISECONDS.toMinutes(sunset);
        hour = TimeUnit.MILLISECONDS.toHours(sunset);
        mWeatherData.sunDown = String.format(mLocal,"%02d:%02d", hour, minute);
        mWeatherData.windSpeed = windObj.getDouble("speed");
        mWeatherData.windDirection = String.valueOf(windObj.getDouble("deg"));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateDisplays();
            }
        });
    }

    @Override
    public void errorHandler(final Context context) {
        mTimer.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Grad nije pronađen");
                builder1.setTitle("Greška");
                builder1.setNeutralButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                        // Switch to MainActivity when exception occurs.
                        Intent switchActivity = new Intent(context, MainActivity.class);
                        startActivity(switchActivity);
                    }
                });
                AlertDialog alert = builder1.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onStop() {
        mTimer.cancel();
        super.onStop();
    }

    private void initDisplays(){
        // Temperature display.
        mTemperatureDisplay = new TemperatureDisplay();
        mTemperatureDisplay.itemsHolder = findViewById(R.id.temp_display);
        mTemperatureDisplay.unitsAdapter = new ArrayAdapter<String>(this,
                                                R.layout.support_simple_spinner_dropdown_item);
        Spinner list = findViewById(R.id.list_units);
        list.setAdapter(mTemperatureDisplay.unitsAdapter);
        list.setOnItemSelectedListener(this);
        mTemperatureDisplay.unitsAdapter.add("C");
        mTemperatureDisplay.unitsAdapter.add("F");
        mTemperatureDisplay.items.put("temperature", (TextView) findViewById(R.id.temp_val));
        mTemperatureDisplay.values.put("temperature", "25");
        mTemperatureDisplay.items.put("humidity", (TextView) findViewById(R.id.humidity_val));
        mTemperatureDisplay.values.put("humidity", "1000");
        mTemperatureDisplay.items.put("pressure",(TextView) findViewById(R.id.pressure_val));
        mTemperatureDisplay.values.put("pressure", "1013");
        mTemperatureDisplay.setValues();
        // Celisus by default
        mTemperatureDisplay.scaleSelected = "C";
        mTemperatureDisplay.show(Boolean.FALSE);
        // Sunrise display.
        mSunriseDisplay = new Display();
        mSunriseDisplay.itemsHolder = findViewById(R.id.sunrise_display);
        mSunriseDisplay.items.put("sunrise_up", (TextView) findViewById(R.id.sunrise_up));
        mSunriseDisplay.values.put("sunrise_up", "6:00");
        mSunriseDisplay.items.put("sunrise_down", (TextView) findViewById(R.id.sunrise_down));
        mSunriseDisplay.values.put("sunrise_down", "18:00");
        mSunriseDisplay.setValues();
        mSunriseDisplay.show(Boolean.FALSE);
        // Wind display.
        mWindDisplay = new Display();
        mWindDisplay.itemsHolder = findViewById(R.id.wind_display);
        mWindDisplay.items.put("wind_speed", (TextView) findViewById(R.id.wind_speed));
        mWindDisplay.values.put("wind_speed", "10");
        mWindDisplay.items.put("wind_direction", (TextView) findViewById(R.id.wind_diretion));
        mWindDisplay.values.put("wind_direction", "N");
        mWindDisplay.setValues();
        mWindDisplay.show(Boolean.FALSE);

    }

    private void updateDisplays()
    {
        mTemperatureDisplay.values.put("pressure", String.valueOf(mWeatherData.pressure));
        mTemperatureDisplay.values.put("temperature", String.valueOf(mWeatherData.temperature));
        mTemperatureDisplay.values.put("humidity", String.valueOf(mWeatherData.humidity));

        mSunriseDisplay.values.put("sunrise_up", mWeatherData.sunUp);
        mSunriseDisplay.values.put("sunrise_down", mWeatherData.sunDown);

        mWindDisplay.values.put("wind_speed", String.valueOf(mWeatherData.windSpeed));
        mWindDisplay.values.put("wind_direction", mWeatherData.windDirection);

        mTemperatureDisplay.setValues();
        mSunriseDisplay.setValues();
        mWindDisplay.setValues();
    }

    private void initOptions(){
        mTemperatureOption = findViewById(R.id.temp_button);
        mTemperatureOption.setOnClickListener(this);

        mSunriseOption = findViewById(R.id.sunrise_button);
        mSunriseOption.setOnClickListener(this);

        mWindOption = findViewById(R.id.wind_button);
        mWindOption.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.temp_button:
                mTemperatureOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSelected));
                mSunriseOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));
                mWindOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));

                mTemperatureDisplay.show(Boolean.TRUE);
                mWindDisplay.show(Boolean.FALSE);
                mSunriseDisplay.show(Boolean.FALSE);
                break;
            case R.id.sunrise_button:
                mTemperatureOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));
                mSunriseOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSelected));
                mWindOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));

                mTemperatureDisplay.show(Boolean.FALSE);
                mSunriseDisplay.show(Boolean.TRUE);
                mWindDisplay.show(Boolean.FALSE);
                break;
            case R.id.wind_button:
                mTemperatureOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));
                mSunriseOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));
                mWindOption.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSelected));

                mTemperatureDisplay.show(Boolean.FALSE);
                mSunriseDisplay.show(Boolean.FALSE);
                mWindDisplay.show(Boolean.TRUE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = mTemperatureDisplay.unitsAdapter.getItem(i);
        if(!mTemperatureDisplay.scaleSelected.equals(item)){
            mTemperatureDisplay.scaleSelected = item;
            double val = Double.valueOf(mTemperatureDisplay.values.get("temperature"));
            if(item.equals("F")){
                val = val * 1.8 + 32.0;
            }
            else{
                val = (val - 32.0) / 1.8;
            }
            // Update value in view object.
            mTemperatureDisplay.values.put("temperature", String.valueOf(val));
            mTemperatureDisplay.setValues();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
