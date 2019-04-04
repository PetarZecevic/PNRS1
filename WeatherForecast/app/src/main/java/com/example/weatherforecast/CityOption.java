package com.example.weatherforecast;

// Class that holds resources that are in ListView options.
public class CityOption {
    private String mCityName;
    private boolean mSelected;

    public CityOption(String name){
        mCityName = name;
        mSelected = false;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }
    public void select() { mSelected = true;}
    public void unselect() { mSelected = false;}
    public String getCityName() { return mCityName; }
    public boolean isSelected() { return mSelected; }
}
