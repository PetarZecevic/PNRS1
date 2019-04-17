package com.example.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.DeadObjectException;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WeatherTimerTask extends TimerTask{
    private String mInfoURL;
    private String mImageURL;
    private String mID;
    private Context mContext;
    private onUpdateListener mListener;
    private HttpHelper mHttpHelper;
    public WeatherTimerTask(String infoURL, String imageURL, String ID, Context context, onUpdateListener listener){
        mInfoURL = infoURL;
        mImageURL = imageURL;
        mID = ID;
        mContext = context;
        mListener = listener;
        mHttpHelper = new HttpHelper();
    }
    @Override
    public void run() {
        try {
            JSONObject weatherJSON = mHttpHelper.getJSONObjectFromURL(mInfoURL);
            Bitmap data = mHttpHelper.getImage(mImageURL +
                    weatherJSON.getJSONArray("weather")
                                    .getJSONObject(0)
                                        .getString("icon")+".png"/*+*"&" +mID*/);
            mListener.update(weatherJSON, data);
        }catch (Exception e){
            e.printStackTrace();
            mListener.errorHandler(mContext);
        }
    }
}
