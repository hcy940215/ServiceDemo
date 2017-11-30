// IBookManager.aidl
package com.shengmingji.servicedemo;
import com.shengmingji.servicedemo.IOnNewBookListener;
// Declare any non-default types here with import statements
import com.shengmingji.servicedemo.Book;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookListener listener);
    void unregisterListener(IOnNewBookListener listener);
}
