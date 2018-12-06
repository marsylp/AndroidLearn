package com.mars.mysimple.binder_aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mars.mysimple.IMyPlayer;
import com.mars.mysimple.R;
import com.mars.mysimple.binder_proxy_stub.PlayerService;

public class PlayerAIDLActivity extends AppCompatActivity {
    private TextView mTvState;
    private IMyPlayer mBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = IMyPlayer.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_aidl);
        mTvState = findViewById(R.id.tvMP3State);
        startService(new Intent(this, MyAIDLService.class));
        bindService(new Intent(this, MyAIDLService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onMP3PlayClick(View view) throws RemoteException {
        if (mBinder == null) return;
        mBinder.play();
        mTvState.setText(mBinder.getState());
    }

    public void onMP3PStopClick(View view) throws RemoteException{
        if (mBinder == null) return;
        mBinder.stop();
        mTvState.setText(mBinder.getState());
    }

    public void onExitClick(View view) {
        stopService(new Intent(this, PlayerService.class));
        unbindService(mConnection);
        finish();
    }
}
