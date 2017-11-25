package com.shengmingji.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {

    private String TAG ="TAG";
    private boolean flg = true;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + Thread.currentThread().getName());
        String msg ="";
        if(intent!=null){
            msg =  intent.getStringExtra("msg");
        }
        final String finalMsg = msg;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
                        Thread.sleep(1000);
                        if (flg){
                            Log.i(TAG, "run: 第 " + (i + 1) + " 个"+"   >>>Activity传过来的"+ finalMsg);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        1.START_STICKY:onStartCommand()方法返回START_STICKY时，如果kill掉当前Service，
//        系统会执行该服务的onCreate()和onStartCommand，但是启动它的Intent为空！(类似于车祸后苏醒，但失忆了)
//
//        2.START_NOT_STICKY:onStartCommand()方法返回START_NOT_STICKY时，如果kill掉当前Service，
//        系统不会执行该服务的onCreate()和onStartCommand！！(类似于车祸后over了)
//
//        3.START_REDELIVER_INTENT:onStartCommand()方法返回START_REDELIVER_INTENT时，如果kill掉当前Service，
//        系统会执行该服务的onCreate()和onStartCommand！！并且启动它的Intent不为空！(类似于车祸后苏醒，还有记忆)
//
//        4.START_STICKY_COMPATIBILITY:onStartCommand()方法返回START_STICKY_COMPATIBILITY时，如果kill掉当前Service，
//        系统会执行该服务的onCreate(),但不会执行onStartCommand！！（不知道类似于啥啦）
        return START_STICKY_COMPATIBILITY;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: " + Thread.currentThread().getName());
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: " + Thread.currentThread().getName());
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {

        flg = false;
        Log.i(TAG, "onDestroy: " + Thread.currentThread());
        super.onDestroy();
    }
}
