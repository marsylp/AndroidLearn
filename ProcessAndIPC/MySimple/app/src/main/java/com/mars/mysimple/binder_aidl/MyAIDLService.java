package com.mars.mysimple.binder_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyAIDLService extends Service {
    private MyBinder mBinder;
    public MyAIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MyBinder(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
