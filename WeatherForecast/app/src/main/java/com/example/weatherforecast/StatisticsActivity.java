package com.example.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "StatisticsActivity";
    private String mCityName;
    private String mLatestDay;
    private String mLatestDate;

    private class Day{
        public TextView dayView, temperatureView, pressureView, humidityView;
        public int temperature;
        public int pressure;
        public double humidity;

        public Day(int dayId, int temperatureId, int pressureId, int humidityId){
            dayView = findViewById(dayId);
            temperatureView = findViewById(temperatureId);
            pressureView = findViewById(pressureId);
            humidityView = findViewById(humidityId);
        }

        public void update(int t, int p, double h){
            temperature = t;
            temperatureView.setText(String.valueOf(t));
            pressure = p;
            pressureView.setText(String.valueOf(p));
            humidity = h;
            humidityView.setText(String.valueOf(h));
        }
    }

    private Day[] mWeekWeatherData;
    private WeatherDbHelper weatherDatabase;
    private int currentDayIndex;
    private boolean coldSelected;
    private boolean hotSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mCityName = getIntent().getStringExtra(MainActivity.CITY_INDEX);
        mLatestDay = getIntent().getStringExtra(DetailsActivity.DAY_INDEX);
        mLatestDate = getIntent().getStringExtra(DetailsActivity.DATE_INDEX);

        TextView header = findViewById(R.id.stats_city_name);
        header.setText(mCityName);

        findViewById(R.id.stats_cold_days).setOnClickListener(this);
        findViewById(R.id.stats_hot_days).setOnClickListener(this);

        mWeekWeatherData = new Day[7];
        weatherDatabase = new WeatherDbHelper(getApplicationContext());
        currentDayIndex = -1;
        coldSelected = false;
        hotSelected = false;
        initWeekStats();
    }

    // Fill Statistics Week-Weather data from database
    private void initWeekStats() {
        // Monday.
        mWeekWeatherData[0] = new Day(R.id.stats_monday, R.id.stats_monday_temp,
                                      R.id.stats_monday_pressure, R.id.stats_monday_humidity);
        getDataForDay(mWeekWeatherData[0]);
        // Tuesday.
        mWeekWeatherData[1] = new Day(R.id.stats_tuesday, R.id.stats_tuesday_temp,
                R.id.stats_tuesday_pressure, R.id.stats_tuesday_humidity);
        getDataForDay(mWeekWeatherData[1]);
        // Wednesday.
        mWeekWeatherData[2] = new Day(R.id.stats_wednesday, R.id.stats_wednesday_temp,
                R.id.stats_wednesday_pressure, R.id.stats_wednesday_humidity);
        getDataForDay(mWeekWeatherData[2]);
        // Thursday.
        mWeekWeatherData[3] = new Day(R.id.stats_thursday, R.id.stats_thursday_temp,
                R.id.stats_thursday_pressure, R.id.stats_thursday_humidity);
        getDataForDay(mWeekWeatherData[3]);
        // Friday.
        mWeekWeatherData[4] = new Day(R.id.stats_friday, R.id.stats_friday_temp,
                R.id.stats_friday_pressure, R.id.stats_friday_humidity);
        getDataForDay(mWeekWeatherData[4]);
        // Saturday.
        mWeekWeatherData[5] = new Day(R.id.stats_saturday, R.id.stats_saturday_temp,
                R.id.stats_saturday_pressure, R.id.stats_saturday_humidity);
        getDataForDay(mWeekWeatherData[5]);
        // Sunday.
        mWeekWeatherData[6] = new Day(R.id.stats_sunday, R.id.stats_sunday_temp,
                R.id.stats_sunday_pressure, R.id.stats_sunday_humidity);
        getDataForDay(mWeekWeatherData[6]);
        // Min temperature.
        setMinTemperature();
        // Max temperature.
        setMaxTemperature();
        // Bold current date.
        selectCurrentDay();
    }

    // Fill data for one day in week
    private void getDataForDay(Day d){

        String day = (String)d.dayView.getText();
        Log.d(LOG_TAG, "Day " + day);
        WeatherData wdata = weatherDatabase.readWeatherData(day, mCityName);
        if(wdata != null)
            d.update((int)wdata.temperature, wdata.pressure, wdata.humidity);
        else
            d.update(8, 1013, 40.1);
    }

    private void setMinTemperature(){
        int minIndex = 0;
        int min = Integer.MAX_VALUE;
        int tmp;
        for(int i = 0; i < 7; i++){
            tmp = mWeekWeatherData[i].temperature;
            if(tmp < min){
                minIndex = i;
                min = tmp;
            }
        }
        TextView minDay = findViewById(R.id.stats_min_temp_day);
        minDay.setText(mWeekWeatherData[minIndex].dayView.getText());
        TextView minValue = findViewById(R.id.stats_min_temp_value);
        minValue.setText(String.valueOf(mWeekWeatherData[minIndex].temperature));
    }

    private void setMaxTemperature(){
        int maxIndex = 0;
        int max = Integer.MIN_VALUE;
        int tmp;
        for(int i = 0; i < 7; i++){
            tmp = mWeekWeatherData[i].temperature;
            if(tmp > max){
                maxIndex = i;
                max = tmp;
            }
        }
        TextView maxDay = findViewById(R.id.stats_max_temp_day);
        maxDay.setText(mWeekWeatherData[maxIndex].dayView.getText());
        TextView maxValue = findViewById(R.id.stats_max_temp_value);
        maxValue.setText(String.valueOf(mWeekWeatherData[maxIndex].temperature));
    }

    private void selectCurrentDay(){
        if(currentDayIndex != -1)
            mWeekWeatherData[currentDayIndex].dayView.setTextColor(
                    getResources().getColor(R.color.colorAccent));
        else
        {
            if(mLatestDay != null && !mLatestDate.isEmpty()) {
                for (int i = 0; i < 7; i++) {
                    String tmpDay = (String) mWeekWeatherData[i].dayView.getText();
                    if (mLatestDay.equals(tmpDay)) {
                        currentDayIndex = i;
                        break;
                    }
                }
                if(currentDayIndex != -1) {
                    mWeekWeatherData[currentDayIndex].dayView.setTextColor(
                            getResources().getColor(R.color.colorAccent));
                }
            }
        }
    }

    private void selectColdDays(){
        for(int i = 0; i < 7; i++){
            if(mWeekWeatherData[i].temperature < 10){
                mWeekWeatherData[i].dayView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void selectHotDays(){
        for(int i = 0; i < 7; i++){
            if(mWeekWeatherData[i].temperature >= 10){
                mWeekWeatherData[i].dayView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void unselectDays(){
        for(int i = 0; i < 7; i++){
            mWeekWeatherData[i].dayView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.stats_cold_days:
                Log.d(LOG_TAG, "Select days which temp is below 10C");
                if(!coldSelected) {
                    unselectDays();
                    selectColdDays();
                    coldSelected = true;
                    hotSelected = false;
                }
                else {
                    coldSelected = false;
                    unselectDays();
                    selectCurrentDay();
                }
                break;
            case R.id.stats_hot_days:
                Log.d(LOG_TAG, "Select days which temp is above 10C");
                if(!hotSelected) {
                    unselectDays();
                    selectHotDays();
                    hotSelected = true;
                    coldSelected = false;
                }else {
                    hotSelected = false;
                    unselectDays();
                    selectCurrentDay();
                }
                break;
            default:
                break;
        }
    }
}
