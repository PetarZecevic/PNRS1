package com.example.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONObject;

public class CallbackWeather extends ICallbackExample.Stub{
    private String mInfoURL;
    private String mImageURL;
    private Context mContext;
    private onUpdateListener mListener;
    private HttpHelper mHttpHelper;

    public CallbackWeather(String infoURL, String imageURL, Context context, onUpdateListener listener){
        mInfoURL = infoURL;
        mImageURL = imageURL;
        mContext = context;
        mListener = listener;
        mHttpHelper = new HttpHelper();
    }

    @Override
    public void onCallbackCall() throws RemoteException {
        Log.d("CallbackWeather", "onCallbackCall");
        try {
            JSONObject weatherJSON = mHttpHelper.getJSONObjectFromURL(mInfoURL);
            Bitmap data = mHttpHelper.getImage(mImageURL +
                    weatherJSON.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("icon")+".png");
            mListener.update(weatherJSON, data);
        }catch (Exception e){
            //mListener.errorHandler(mContext);
            Log.d("CallbackWeather", e.getMessage());
        }
    }
}
