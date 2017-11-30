package com.shengmingji.servicedemo.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

import static com.shengmingji.servicedemo.binderpool.BinderPool.BINDER_COMPUTE;
import static com.shengmingji.servicedemo.binderpool.BinderPool.BINDER_SECURITY_CENTER;

/**
 * Created by dell on 2017/11/30.
 */

public class BinderPoolImpl extends IBinderPool.Stub {


    @Override
    public IBinder queryBinder(int code) throws RemoteException {
        switch (code) {
            case BINDER_SECURITY_CENTER:
                return new SecurityImpl();
            case BINDER_COMPUTE:
                return new ComputeImpl();
        }

        return null;
    }
}
