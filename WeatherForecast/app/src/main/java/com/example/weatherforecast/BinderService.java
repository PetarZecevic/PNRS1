package com.example.weatherforecast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BinderService extends Service {
    private BinderExample mBinderExample = null;

    public BinderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinderExample == null) {
            mBinderExample = new BinderExample();
        }
        return mBinderExample;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBinderExample.stop();
        return super.onUnbind(intent);
    }
}
