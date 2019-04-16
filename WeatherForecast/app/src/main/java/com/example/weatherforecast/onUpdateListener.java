package com.example.weatherforecast;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public interface onUpdateListener {
    void update(JSONObject jsonObject) throws JSONException;
    void errorHandler(Context context);
}
