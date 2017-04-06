package com.onlyhiedu.mobile.Model.http;

import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/3/17.
 */

public abstract class MyResourceSubscriber<T> extends ResourceSubscriber<T> implements Callback<T>{


    @Override
    public void onNext(T data) {
        onNextData(data);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        Toast.makeText(App.getInstance().getApplicationContext(), Constants.NET_ERROR, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
    }

}
