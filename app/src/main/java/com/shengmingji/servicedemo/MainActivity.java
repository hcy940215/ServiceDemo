package com.shengmingji.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private String TAG = MainActivity.class.getSimpleName();
    private MyServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MyService.class);

    }

    public void stopService(View view) {
        stopService(intent);
    }

    public void unBindService(View view) {
        unbindService(connection);
    }

    public void startService(View view) {
        intent.putExtra("msg","开启Service");
        startService(intent);
    }

    public void bindService(View view) {
        connection = new MyServiceConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + Thread.currentThread());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + Thread.currentThread());

        }
    }
}
