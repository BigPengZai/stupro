package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.UI.User.presenter.contract.RegContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

import static android.R.attr.value;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RegPresenter extends RxPresenter<RegContract.View> implements RegContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public RegPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void readSecond() {
        Flowable<Long> flowable = Flowable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS);

        MyResourceSubscriber observer = new MyResourceSubscriber<Long>() {
            @Override
            public void onNextData(Long value) {
                if (getView() != null)
                    getView().showSecond(60 - new Long(value).intValue());
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
