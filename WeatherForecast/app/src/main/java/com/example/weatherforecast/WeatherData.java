package com.example.weatherforecast;

import android.graphics.Bitmap;

public class WeatherData {
    public String date;
    public String day;
    public double temperature;
    public double humidity;
    public int pressure;
    public String sunUp;
    public String sunDown;
    public double windSpeed;
    public String windDirection;
    public Bitmap weatherIcon;
    WeatherData(){
        date = "";
        day = "";
        temperature = 0;
        humidity = 0;
        pressure = 0;
        sunUp = "0";
        sunDown = "1";
        windSpeed = 0;
        windDirection = "0";
        weatherIcon = null;
    }
}
