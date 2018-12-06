package com.mars.mysimple.binder_simple;

import android.os.Binder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MyBinder extends Binder {
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        String handlerState;
        switch (code) {
            case 1001:
                //将Message丢到子线程的MQ to play MP3
                handlerState = MyService.PLAY;
                Message playMessage = MyService.mHandler.obtainMessage(1, 1, 1001, handlerState);
                MyService.mHandler.sendMessage(playMessage);
                break;
            case 1002:
                //将Message丢到子线程的MQ to stop playing
                handlerState = MyService.STOP;
                Message stopMessage = MyService.mHandler.obtainMessage(1, 1, 1002, handlerState);
                MyService.mHandler.sendMessage(stopMessage);
                break;
            default:
                break;
        }
        return true;
    }
}
