package com.mars.mysimple.binder_simple;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mars.mysimple.R;

public class MainBinderActivity extends AppCompatActivity {

    private TextView mMp3State;
    private IBinder mIBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinder = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_binder);
        mMp3State = findViewById(R.id.tvMP3State);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_B_EVENT);
        registerReceiver(new MyReceiver(), intentFilter);
        bindService();
    }

    private void bindService() {
        startService(new Intent(MainBinderActivity.this, MyService.class));
        bindService(new Intent(MainBinderActivity.this, MyService.class),
                mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onMP3PlayClick(View view) {
        if (mIBinder == null) {
            bindService();
        }
        Log.e("xxx", "Activity process:" + android.os.Process.myPid());
        Parcel data = Parcel.obtain();
        data.writeString("播放音乐");
        Parcel reply = Parcel.obtain();
        try {
            mIBinder.transact(1001, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onMP3PStopClick(View view) {
        if (mIBinder == null) {
            bindService();
        }
        Parcel data = Parcel.obtain();
        data.writeString("暂停音乐");
        Parcel reply = Parcel.obtain();
        try {
            mIBinder.transact(1002, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int key = intent.getIntExtra("key", 0);
            if (key == 1001) {
                mMp3State.setText("Service接收到播放信息");
            } else if (key == 1002) {
                mMp3State.setText("Service接收到暂停信息");
            } else {
                mMp3State.setText("没有接收到消息");
            }
        }
    }
}
