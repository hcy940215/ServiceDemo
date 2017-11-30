// ISecurity.aidl
package com.shengmingji.servicedemo.binderpool;

// Declare any non-default types here with import statements

interface ISecurity {
    String encryt(String content);
    String decryt(String pwd);
}
