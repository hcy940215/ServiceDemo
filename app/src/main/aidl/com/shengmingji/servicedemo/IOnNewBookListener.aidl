// IOnNewBookListener.aidl
package com.shengmingji.servicedemo;

// Declare any non-default types here with import statements
import com.shengmingji.servicedemo.Book;
interface IOnNewBookListener {
  void onNewBookListener(in Book newBook);
}
