package com.mars.mysimple.binder_messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mars.mysimple.R;

public class MessengerActivity extends AppCompatActivity {
    private Messenger mBinderMsg;
    private Messenger mMessenger;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderMsg = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinderMsg = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        mMessenger = new Messenger(new MyHandler());
        bindService(new Intent(this, MyMessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onCallClick(View view) throws RemoteException {
        Message message = Message.obtain(null, 1, "我是Activity，呼叫Service");
        message.replyTo = mMessenger;
        mBinderMsg.send(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("xxx", "Activity -> msg: " + msg.obj);
        }
    }

}
