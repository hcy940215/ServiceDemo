package com.shengmingji.servicedemo.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shengmingji.servicedemo.R;
import com.shengmingji.servicedemo.utils.MyConstants;

public class MessengerActivity extends AppCompatActivity {

    private Messenger messenger;
    private String tag = "TAG";
    private MyServiceConnection conn;


    public class ClientMessageHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants.MSG_FROM_SERVICE:
                    Log.i(tag, "handleMessage: "+msg.getData().getString("reply"));
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerSeivice.class);
        conn = new MyServiceConnection();
        bindService(intent, conn,BIND_AUTO_CREATE);
    }

    public class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(tag, "onServiceConnected: 执行了");
            messenger = new Messenger(service);
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg","hello, this is client!!");
            msg.setData(bundle);
            msg.replyTo=new Messenger(new ClientMessageHandle());
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}
