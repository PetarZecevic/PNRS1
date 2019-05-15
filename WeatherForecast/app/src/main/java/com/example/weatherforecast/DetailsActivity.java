package com.example.weatherforecast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, onUpdateListener{
    public final static String LOG_TAG = "DetailsActivity";
    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final static String IMG_URL = "http://api.openweathermap.org/img/w/";
    private final static String METRIC = "units=metric";
    private final static String APPID = "APPID=43316f9684dd3d0af930ecbb1b39fb35";
    private String mCityName;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormat;
    private TextView mLocation, mDate, mUpdateText;
    private ImageButton mUpdateButton;
    private Button mTemperatureOption, mSunriseOption, mWindOption, mStatisticsButton;
    private Display mWindDisplay, mSunriseDisplay;
    private Locale mLocal;
    private WeatherTask mRefresher;
    private WeatherData mWeatherData;
    private WeatherDbHelper weatherDatabase;
    public static String currentDay;

    private class TemperatureDisplay extends Display{
        public ArrayAdapter<String> unitsAdapter;
        public String scaleSelected;
        public ImageView weatherIcon;
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
        mCityName = getIntent().getStringExtra(MainActivity.CITY_INDEX);
        mLocation = findViewById(R.id.location);
        mLocation.setText(mCityName);
        mDate = findViewById(R.id.date);
        mUpdateText = findViewById(R.id.update_text);
        mUpdateButton = findViewById(R.id.update_button);
        mUpdateButton.setOnClickListener(this);
        findViewById(R.id.stats_button).setOnClickListener(this);

        mLocal = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        mDateFormat = new SimpleDateFormat("dd.MM.yyyy", mLocal);

        mRefresher = new WeatherTask(BASE_URL + mCityName + "&" + METRIC + "&" + APPID,
                IMG_URL,this, this);
        mWeatherData = new WeatherData();
        weatherDatabase = new WeatherDbHelper(getApplicationContext());

        initDisplays();
        initOptions();
        getData();
    }

    /**
     * Try to get weather data from database and if it does not exist in database,
     * get it from server.
     */
    private void getData() {
        // Get day and date.
        String day = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, mLocal);
        day = day.substring(0,1).toUpperCase(mLocal) + day.substring(1);
        currentDay = day;
        String currentDate = mDateFormat.format(mCalendar.getTime());

        WeatherData wdata = weatherDatabase.readApproximationData(mCityName);
        if(wdata == null){
            // Not found in database, get data from server.
            Thread t = new Thread(mRefresher);
            t.start();
        }
        else{
            // Found data in database.
            mWeatherData = wdata;
            updateDisplays();
            // Check if date from database matches the current date.
            if(mWeatherData.date.equals(currentDate)){
                mUpdateText.setVisibility(View.INVISIBLE);
            }
            else{
                mUpdateText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initDisplays(){
        // Temperature display.
        mTemperatureDisplay = new TemperatureDisplay();
        mTemperatureDisplay.itemsHolder = findViewById(R.id.temp_display);
        mTemperatureDisplay.unitsAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item);
        Spinner list = findViewById(R.id.list_units);
        list.setAdapter(mTemperatureDisplay.unitsAdapter);
        list.setOnItemSelectedListener(this);
        mTemperatureDisplay.unitsAdapter.add("C");
        mTemperatureDisplay.unitsAdapter.add("F");
        mTemperatureDisplay.items.put("temperature", (TextView) findViewById(R.id.temp_val));
        mTemperatureDisplay.values.put("temperature", null);
        mTemperatureDisplay.items.put("humidity", (TextView) findViewById(R.id.humidity_val));
        mTemperatureDisplay.values.put("humidity", null);
        mTemperatureDisplay.items.put("pressure",(TextView) findViewById(R.id.pressure_val));
        mTemperatureDisplay.values.put("pressure", null);
        mTemperatureDisplay.weatherIcon = findViewById(R.id.weather_icon);
        mTemperatureDisplay.setValues();
        // Celisus by default
        mTemperatureDisplay.scaleSelected = "C";
        mTemperatureDisplay.show(Boolean.FALSE);
        // Sunrise display.
        mSunriseDisplay = new Display();
        mSunriseDisplay.itemsHolder = findViewById(R.id.sunrise_display);
        mSunriseDisplay.items.put("sunrise_up", (TextView) findViewById(R.id.sunrise_up));
        mSunriseDisplay.values.put("sunrise_up", null);
        mSunriseDisplay.items.put("sunrise_down", (TextView) findViewById(R.id.sunrise_down));
        mSunriseDisplay.values.put("sunrise_down", null);
        mSunriseDisplay.setValues();
        mSunriseDisplay.show(Boolean.FALSE);
        // Wind display.
        mWindDisplay = new Display();
        mWindDisplay.itemsHolder = findViewById(R.id.wind_display);
        mWindDisplay.items.put("wind_speed", (TextView) findViewById(R.id.wind_speed));
        mWindDisplay.values.put("wind_speed", null);
        mWindDisplay.items.put("wind_direction", (TextView) findViewById(R.id.wind_diretion));
        mWindDisplay.values.put("wind_direction", null);
        mWindDisplay.setValues();
        mWindDisplay.show(Boolean.FALSE);
    }

    private void updateDisplays() {
        synchronized (mTemperatureDisplay) {
            mTemperatureDisplay.values.put("pressure", String.valueOf(mWeatherData.pressure));
            if(mTemperatureDisplay.scaleSelected.equals("F"))
                mWeatherData.temperature = changeTempScale(mWeatherData.temperature, true);
            mTemperatureDisplay.values.put("temperature", String.valueOf(mWeatherData.temperature));
            mTemperatureDisplay.values.put("humidity", String.valueOf(mWeatherData.humidity));
            mTemperatureDisplay.weatherIcon.setImageBitmap(mWeatherData.weatherIcon);
            mTemperatureDisplay.setValues();
        }

        mSunriseDisplay.values.put("sunrise_up", mWeatherData.sunUp);
        mSunriseDisplay.values.put("sunrise_down", mWeatherData.sunDown);
        mSunriseDisplay.setValues();

        mWindDisplay.values.put("wind_speed", String.valueOf(mWeatherData.windSpeed));
        mWindDisplay.values.put("wind_direction", mWeatherData.windDirection);
        mWindDisplay.setValues();

        // Set current date.
        mDate.setText(mWeatherData.date);
        mUpdateText.setVisibility(View.INVISIBLE);
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
    public void update(JSONObject jsonObject, Bitmap imageData) throws JSONException {
        Log.d(LOG_TAG, "JSON: " + jsonObject.toString());
        JSONObject sysObj = jsonObject.getJSONObject("sys"); // sunset.
        JSONObject mainObj = jsonObject.getJSONObject("main"); // temperature, humidity, pressure.
        JSONObject windObj = jsonObject.getJSONObject("wind");

        String day = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, mLocal);
        day = day.substring(0,1).toUpperCase(mLocal) + day.substring(1);

        mWeatherData.date = mDateFormat.format(mCalendar.getTime());
        mWeatherData.day = day;
        mWeatherData.temperature = mainObj.getInt("temp");
        mWeatherData.humidity = mainObj.getDouble("humidity");
        mWeatherData.pressure = mainObj.getInt("pressure");
        // Convert unix timestamp to clock format.
        long sunrise = sysObj.getLong("sunrise")*1000;
        long sunset = sysObj.getLong("sunset")*1000;
        Date dsunrise = new Date(sunrise);
        Date dsunset = new Date(sunset);
        mWeatherData.sunUp = new SimpleDateFormat("HH:mm", mLocal).format(dsunrise);
        mWeatherData.sunDown = new SimpleDateFormat("HH:mm", mLocal).format(dsunset);
        mWeatherData.windSpeed = windObj.getDouble("speed");
        mWeatherData.windDirection = degreeToCardinal(windObj.getDouble("deg"));
        // Weather Image.
        mWeatherData.weatherIcon = imageData;
        weatherDatabase.insert(mWeatherData, mCityName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateDisplays();
            }
        });
    }

    @Override
    public void errorHandler(final Context context) {
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
            case R.id.update_button:
                Thread t = new Thread(mRefresher);
                t.start();
                break;
            case R.id.stats_button:
                // Switch to Statistics.
                Intent intent = new Intent(this, StatisticsActivity.class);
                intent.putExtra(MainActivity.CITY_INDEX, mCityName);
                startActivity(intent);
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        synchronized (mTemperatureDisplay) {
            String item = mTemperatureDisplay.unitsAdapter.getItem(i);
            if (!mTemperatureDisplay.scaleSelected.equals(item)) {
                mTemperatureDisplay.scaleSelected = item;
                int val = Integer.valueOf(mTemperatureDisplay.values.get("temperature"));
                if (item.equals("F")) {
                    val = changeTempScale(val, true);
                } else {
                    val = changeTempScale(val, false);
                }
                // Update value in view object.
                mTemperatureDisplay.values.put("temperature", String.valueOf(val));
                mTemperatureDisplay.setValues();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private int changeTempScale(int tempValue, boolean FC) {
        int result = 0;
        if(FC)
            // Fahrenheit.
            result = (int)((double)tempValue * 1.8 + 32);
        else
            // Celsius.
            result = (int)(((double)tempValue - 32) / 1.8);
        return result;
    }

    // Convert degree wind direction to cardinal direction:
    //      example: 80 -> E
    private String degreeToCardinal(double degree){
        String direction = "";
        if(degree >= 348.75 || degree < 11.25)
            direction += "N";
        else if(degree >= 11.25 && degree < 33.75)
            direction += "NNE";
        else if(degree >= 33.75 && degree < 56.25)
            direction += "NE";
        else if(degree >= 56.25 && degree < 78.75)
            direction += "ENE";
        else if(degree >= 78.75 && degree < 101.25)
            direction += "E";
        else if(degree >= 101.25 && degree < 123.75)
            direction += "ESE";
        else if(degree >= 123.75 && degree < 146.25)
            direction += "SE";
        else if(degree >= 146.25 && degree < 168.75)
            direction += "SSE";
        else if(degree >= 168.75 && degree < 191.25)
            direction += "S";
        else if(degree >= 191.25 && degree < 213.75)
            direction += "SSW";
        else if(degree >= 213.75 && degree < 236.25)
            direction += "SW";
        else if(degree >= 236.25 && degree < 258.75)
            direction += "WSW";
        else if(degree >= 258.75 && degree < 281.25)
            direction += "W";
        else if(degree >= 281.25 && degree < 303.75)
            direction += "WNW";
        else if(degree >= 303.75 && degree < 326.25)
            direction += "NW";
        else
            direction += "NNW";
        return direction;
    }
}
