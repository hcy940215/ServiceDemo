package com.shengmingji.servicedemo.manualbinder;

import android.os.IInterface;
import android.os.RemoteException;

import com.shengmingji.servicedemo.Book;

import java.util.List;


public interface IBookManager extends IInterface {

    public void addBook(Book book) throws RemoteException;
    public List<Book> getBookList() throws RemoteException;
}
