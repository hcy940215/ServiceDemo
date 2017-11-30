package com.shengmingji.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    public BookManagerService() {
    }
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    CopyOnWriteArrayList<Book> mBookList =
            new CopyOnWriteArrayList<>();

    RemoteCallbackList<IOnNewBookListener> mListener =
            new RemoteCallbackList<>();

    private String TAG = "TAG";
    public Binder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Log.i(TAG, "onTransact: ");
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void registerListener(IOnNewBookListener listener) throws RemoteException {
            mListener.register(listener);
            int N = mListener.beginBroadcast();
            mListener.finishBroadcast();
            Log.i(TAG, "registerListener: current size>>>" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookListener listener) throws RemoteException {
            boolean success = mListener.unregister(listener);
            if (success) {
                Log.i(TAG, "unregisterListener: success");
            } else {
                Log.i(TAG, "not found, can not unregister.");
            }

            int N = mListener.beginBroadcast();
            mListener.finishBroadcast();
            Log.i(TAG, "unregisterListener: current size>>>" + N);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private class  ServiceWorker implements Runnable{

        @Override
        public void run() {
           while (!mIsServiceDestoryed.get()){
               try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               int i = mBookList.size() + 1;

               Book book = new Book(i, "new book#" + i);
               onNewBook(book);
           }
        }
    }

    private void onNewBook(Book book) {
        mBookList.add(book);
        int i = mListener.beginBroadcast();
        for (int j = 0; j < i; j++) {
            IOnNewBookListener l = mListener.getBroadcastItem(j);
            if (l != null) {
                try {
                    l.onNewBookListener(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        mListener.finishBroadcast();
    }


    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();

    }
}
