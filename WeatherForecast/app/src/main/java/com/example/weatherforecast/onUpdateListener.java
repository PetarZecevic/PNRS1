package com.example.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

public interface onUpdateListener {
    void update(JSONObject jsonObject, Bitmap imageData) throws JSONException;
    void errorHandler(Context context);
}
