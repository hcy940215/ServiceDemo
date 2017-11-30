package com.shengmingji.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shengmingji.servicedemo.manager.UserManager;
import com.shengmingji.servicedemo.model.User;
import com.shengmingji.servicedemo.utils.MyConstants;
import com.shengmingji.servicedemo.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private String TAG = "TAG";
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



    public void bindService(View view) {
        connection = new MyServiceConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "UserManage.sUserId=" + UserManager.sUserId);
        persistToFile();

        super.onResume();
    }

    private void persistToFile() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                User user = new User(1, "hello world", false);
                File dir = new File(MyConstants.CHAPTER_2_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cachedFile = new File(MyConstants.CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(
                            new FileOutputStream(cachedFile));
                    objectOutputStream.writeObject(user);
                    Log.d(TAG, "persist user:" + user);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    MyUtils.close(objectOutputStream);
                }
            }
        }).start();
    }

    public void openActivityB(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SecondActivity.class);
        User user = new User(0, "jake", true);
        user.book = new Book();
        intent.putExtra("extra_user", (Serializable) user);
        startActivity(intent);
    }
}
