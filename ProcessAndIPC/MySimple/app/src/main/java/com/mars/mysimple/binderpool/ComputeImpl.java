package com.mars.mysimple.binderpool;

import android.os.RemoteException;

import com.mars.mysimple.ICompute;

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
