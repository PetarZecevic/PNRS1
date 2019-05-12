package com.example.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;
import org.json.JSONObject;


public class WeatherTask implements Runnable{
    private String mInfoURL;
    private String mImageURL;
    private Context mContext;
    private onUpdateListener mListener;
    private HttpHelper mHttpHelper;
    public WeatherTask(String infoURL, String imageURL, Context context, onUpdateListener listener){
        mInfoURL = infoURL;
        mImageURL = imageURL;
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
                                        .getString("icon")+".png");
            mListener.update(weatherJSON, data);
        }catch (Exception e){
            e.printStackTrace();
            mListener.errorHandler(mContext);
        }
    }
}
