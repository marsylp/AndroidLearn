package com.mars.mysimple.binder_proxy_stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PlayerService extends Service {
    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBinder(this);
    }
}
