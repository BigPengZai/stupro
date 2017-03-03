package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.UI.User.presenter.contract.SmsLoginContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/3/3.
 */

public class SmsLoginPresenter extends RxPresenter<SmsLoginContract.View> implements SmsLoginContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public SmsLoginPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void readSecond() {

        Flowable<Long> flowable = Flowable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS);

        ResourceSubscriber observer = new ResourceSubscriber<Long>() {
            @Override
            public void onNext(Long value) {
                if (getView() != null)
                    getView().showSecond(60 - new Long(value).intValue());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
