package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.RegContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

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


    @Override
    public void registerUser(String userName, String phone, String pwd) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchRegisterInfo(userName,phone, pwd);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showSuccess(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getAuthCode(String phone) {
        Flowable<onlyHttpResponse<AuthCodeInfo>> flowable = mRetrofitHelper.fetchAuthCode(phone);
        MyResourceSubscriber<onlyHttpResponse<AuthCodeInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<AuthCodeInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthCodeInfo> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showAuthSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable,observer));
    }
}
