package com.mars.mysimple.binder_messenger;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyMessengerService extends Service {
    private final Messenger mMessenger = new Messenger(new MyHandler());
    public MyMessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("xxx","Service -> msg:" + msg.obj);
            Message message = Message.obtain(null,0,"收到收到，我是Service!");
            Messenger messenger = msg.replyTo;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
