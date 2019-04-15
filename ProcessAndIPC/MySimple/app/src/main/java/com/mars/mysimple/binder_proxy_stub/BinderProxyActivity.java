package com.mars.mysimple.binder_proxy_stub;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mars.mysimple.R;

public class BinderProxyActivity extends AppCompatActivity {
    private PlayerProxy mPlayerProxy;
    private TextView mState;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerProxy = new PlayerProxy((Binder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_proxy);
        mState = findViewById(R.id.tvMP3State);
        startService(new Intent(this, PlayerService.class));
        bindService(new Intent(this, PlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onMP3PlayClick(View view) {
        mPlayerProxy.play();
        mState.setText(mPlayerProxy.getState());
    }

    public void onMP3PStopClick(View view) {
        mPlayerProxy.stop();
        mState.setText(mPlayerProxy.getState());
    }

    public void onExitClick(View view) {
        stopService(new Intent(this, PlayerService.class));
        unbindService(mConnection);
        finish();
    }
}
