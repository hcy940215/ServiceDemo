package com.shengmingji.servicedemo.binderpool;

import android.os.RemoteException;

import com.shengmingji.servicedemo.binderpool.ISecurity.Stub;

/**
 * Created by dell on 2017/11/30.
 */

public class SecurityImpl extends Stub {
    public static final char SECRET_CODE = '^';

    @Override
    public String encryt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decryt(String pwd) throws RemoteException {
        return encryt(pwd);
    }
}
