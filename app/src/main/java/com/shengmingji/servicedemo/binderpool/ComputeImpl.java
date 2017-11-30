package com.shengmingji.servicedemo.binderpool;

import android.os.RemoteException;

/**
 * Created by dell on 2017/11/30.
 */

public class ComputeImpl  extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
