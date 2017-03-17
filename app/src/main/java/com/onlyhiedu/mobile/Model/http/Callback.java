package com.onlyhiedu.mobile.Model.http;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface Callback<T> {
    void onNextData(T data);
}
