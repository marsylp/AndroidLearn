package com.mars.mysimple;

import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mars.mysimple.binder_aidl.PlayerAIDLActivity;
import com.mars.mysimple.binder_messenger.MessengerActivity;
import com.mars.mysimple.binder_simple.MainBinderActivity;
import com.mars.mysimple.binder_proxy_stub.BinderProxyActivity;
import com.mars.mysimple.binderpool.BinderPoolActivity;
import com.mars.mysimple.socket.TCPClientActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentProvider provider;
    }

    public void onLaunchBinderClick(View view) {
        startActivity(new Intent(this, MainBinderActivity.class));
    }

    public void onBinderProxyClick(View view) {
        startActivity(new Intent(this, BinderProxyActivity.class));
    }

    public void onBinderAIDLClick(View view) {
        startActivity(new Intent(this, PlayerAIDLActivity.class));
    }

    public void onBinderMessengerClick(View view) {
        startActivity(new Intent(this, MessengerActivity.class));
    }

    public void onBinderPoolClick(View view) {
        startActivity(new Intent(this, BinderPoolActivity.class));
    }

    public void onTCPClick(View view) {
        startActivity(new Intent(this, TCPClientActivity.class));
    }
}
