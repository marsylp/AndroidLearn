package com.mars.mysimple.binder_simple;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mars.mysimple.R;

public class MyService extends Service implements Runnable {
    public static Handler mHandler;
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    public static final String MY_B_EVENT = "com.mars.mysimple.binder.MY_B_EVENT";
    private Context mContext;
    private Binder mBinder;
    private MediaPlayer player;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mBinder = new MyBinder();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new EventHandler(Looper.myLooper());
        Looper.loop();
    }

    class EventHandler extends Handler {
        EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("xxx", "Service process:" + android.os.Process.myPid());
            String obj = (String) msg.obj;
            if (player == null) {
                player = MediaPlayer.create(mContext, R.raw.text);
            }
            Intent intent;
            switch (obj) {
                case PLAY:
                    player.start();
                    intent = new Intent();
                    intent.setAction(MY_B_EVENT);
                    intent.putExtra("key", 1001);
                    mContext.sendBroadcast(intent);
                    break;
                case STOP:
                    player.stop();
                    intent = new Intent();
                    intent.setAction(MY_B_EVENT);
                    intent.putExtra("key", 1002);
                    mContext.sendBroadcast(intent);
                    player.release();
                    player = null;
                    break;
                default:
                    break;
            }
        }
    }
}
