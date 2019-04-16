package com.example.weatherforecast;

public class WeatherData {
    public double temperature;
    public double humidity;
    public double pressure;
    public String sunUp;
    public String sunDown;
    public double windSpeed;
    public String windDirection;

    WeatherData(){
        temperature = 0;
        humidity = 0;
        pressure = 0;
        sunUp = "0";
        sunDown = "1";
        windSpeed = 0;
        windDirection = "0";
    }
}
