package com.example.weatherforecast;

// Class that holds resources that are in ListView options.
public class CityOption {
    private String mCityName;

    public CityOption(String name){
        mCityName = name;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getCityName() {
        return mCityName;
    }
}
