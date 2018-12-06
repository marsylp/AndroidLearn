package com.mars.mysimple.binder_proxy_stub;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BaseBinderStub extends Binder implements IPlayer {
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags)
            throws RemoteException {
        if (code == 1001) {
            play();
        } else if (code == 1002) {
            stop();
        }
        return true;
    }

}
