package com.example.weatherforecast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

// Background service for refreshing weather data, with predetermined frequency
public class WeatherService extends Service {
    private static final String LOG_TAG = "ExampleService";
    private static final long PERIOD = 10000L;
    private ServerCommunication mClient = null;
    public static String infoURL = null;
    public static String imageURL = null;
    public static onUpdateListener listener = null;
    public static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mClient = new ServerCommunication(infoURL, imageURL, context, listener);
        mClient.start();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Destroy service");
        super.onDestroy();
        mClient.exit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class ServerCommunication extends Thread {
        private boolean mRun;
        private String mInfoUrl;
        private String mImageUrl;
        private Context mContext;
        private onUpdateListener mListener;
        private HttpHelper mHttpHelper;

        public ServerCommunication(String infUrl, String imgUrl, Context con, onUpdateListener l)
        {
            mInfoUrl = infUrl;
            mImageUrl = imgUrl;
            mContext = con;
            mListener = l;
            mHttpHelper = new HttpHelper();
        }

        @Override
        public synchronized void start() {
            mRun = true;
            super.start();
        }

        public synchronized void exit() {
            mRun = false;
        }

        public synchronized boolean isActive(){
            return mRun;
        }

        @Override
        public void run() {
            while(isActive()) {
                // Ensure update frequency.
                try{
                    Thread.sleep(PERIOD); //milliseconds
                }
                catch(InterruptedException e){
                    // interrupted finish thread
                    Log.d(LOG_TAG, "Interrupted, exit client communication");
                    break;
                }

                try {
                    JSONObject weatherJSON = mHttpHelper.getJSONObjectFromURL(mInfoUrl);
                    Bitmap data = mHttpHelper.getImage(mImageUrl +
                            weatherJSON.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("icon")+".png");
                    mListener.update(weatherJSON, data);
                    mListener.sendNotification(mContext);
                }catch (Exception e){
                    Log.d(LOG_TAG, e.getMessage());
                }
            }
        }
    }
}
