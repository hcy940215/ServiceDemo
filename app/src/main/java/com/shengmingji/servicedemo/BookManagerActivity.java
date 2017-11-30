package com.shengmingji.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private String TAG = "TAG";
    public  static final int MESSAGE_NEW_BOOK = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==MESSAGE_NEW_BOOK){
                Log.i(TAG, "handleMessage: "+msg.obj);
            }
            super.handleMessage(msg);
        }
    };
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> bookList = iBookManager.getBookList();
                Log.i(TAG, "query book list, list type:"
                        + bookList.getClass().getCanonicalName());
                Log.i(TAG, "onServiceConnected: "+bookList.toString());

                Book book = new Book(3, "艺术探索");
                iBookManager.addBook(book);
                List<Book> list = iBookManager.getBookList();
                Log.i(TAG, "onServiceConnected: "+list.toString());
                iBookManager.registerListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IOnNewBookListener mListener = new IOnNewBookListener.Stub() {
        @Override
        public void onNewBookListener(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK,newBook).sendToTarget();
        }
    };
    private IBookManager iBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        try {
            iBookManager.unregisterListener(mListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void testANR(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    iBookManager.getBookList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
