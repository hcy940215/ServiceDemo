package com.shengmingji.servicedemo.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shengmingji.servicedemo.R;

public class BinderPoolActivity extends AppCompatActivity {

    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();


    }

    private void doWork() {
        String msg = "hello 安卓";
        Log.i(TAG, msg);
        BinderPool binderPool = BinderPool.getInsance(this);
        Log.i(TAG, msg);
        IBinder secrityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        ISecurity mSecurtyCenter = SecurityImpl.asInterface(secrityBinder);
        try {
            String encryt = mSecurtyCenter.encryt(msg);
            Log.i(TAG, encryt);
            Log.i(TAG, mSecurtyCenter.decryt(encryt));

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        IBinder compute = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);

        ICompute iCompute = ComputeImpl.asInterface(compute);
        try {
            int add = iCompute.add(3, 5);
            Log.i(TAG, "doWork: >>>>" + add);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
