package com.example.weatherforecast;

import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class BinderExample extends IBinderExample.Stub {
    private static final String TAG = "BinderExample";
    private ICallbackExample mCallback;
    private CallbackCaller mCaller = new CallbackCaller();
    private Timer mTimer = null;
    private final long mRefreshPeriod = 60000L; // in milliseconds.

    @Override
    public void setCallback(ICallbackExample callback) throws RemoteException {
        mCallback = callback;
        mCaller = new CallbackCaller();
        if(mTimer != null){
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(mCaller, 0, mRefreshPeriod);
    }

    public void stop() {
        mTimer.cancel();
    }

    private class CallbackCaller extends TimerTask {
        @Override
        public void run() {
            try {
                mCallback.onCallbackCall();
            } catch (NullPointerException e) {
                // callback is null, do nothing
            } catch (RemoteException e) {
                Log.e(TAG, "onCallbackCall failed", e);
            }
        }
    }
}
