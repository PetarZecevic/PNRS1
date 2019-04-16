package com.example.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.DeadObjectException;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WeatherTimerTask extends TimerTask{
    private String mUrlString;
    private Context mContext;
    private onUpdateListener mListener;
    private HttpHelper mHttpHelper;
    public WeatherTimerTask(String URL, Context context, onUpdateListener listener){
        mUrlString = URL;
        mContext = context;
        mListener = listener;
        mHttpHelper = new HttpHelper();
    }
    @Override
    public void run() {
        try {
            JSONObject weatherJSON = mHttpHelper.getJSONObjectFromURL(mUrlString);
            mListener.update(weatherJSON);
        }catch (Exception e){
            e.printStackTrace();
            mListener.errorHandler(mContext);
        }
    }
}
