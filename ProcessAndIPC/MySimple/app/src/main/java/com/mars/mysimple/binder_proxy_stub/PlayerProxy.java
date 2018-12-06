package com.mars.mysimple.binder_proxy_stub;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class PlayerProxy implements IPlayer {
    private IBinder mIBinder;
    private String mState;

    public PlayerProxy(IBinder IBinder) {
        mIBinder = IBinder;
    }

    @Override
    public void play() {
        Parcel data = Parcel.obtain();
        data.writeString("playing");
        Parcel reply = Parcel.obtain();
        try {
            mIBinder.transact(1001, data, reply, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mState = "点击了播放按钮";
    }

    @Override
    public void stop() {
        Parcel data = Parcel.obtain();
        data.writeString("stop");
        Parcel reply = Parcel.obtain();
        try {
            mIBinder.transact(1002, data, reply, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mState = "点击了停止按钮";
    }

    @Override
    public String getState() {
        return mState;
    }
}
