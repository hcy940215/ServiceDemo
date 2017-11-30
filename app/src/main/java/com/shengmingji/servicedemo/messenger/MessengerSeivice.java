package com.shengmingji.servicedemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.shengmingji.servicedemo.utils.MyConstants;

public class MessengerSeivice extends Service {
    private String TAG = "TAG";

    public MessengerSeivice() {
    }

    public class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG, "handleMessage: " + msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","已经收到消息");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

            super.handleMessage(msg);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new MessengerHandler()).getBinder();
    }
}
