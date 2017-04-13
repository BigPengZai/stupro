package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.mobile.Utils.SPUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

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

        MyResourceSubscriber observer = new MyResourceSubscriber<Long>() {
            @Override
            public void onNextData(Long data) {
                if (getView() != null)
                    getView().showSecond(60 - new Long(data).intValue());
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
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    //验证码登录
    @Override
    public void authLogin(String authCode, String phone, String deviceId) {

        Flowable<onlyHttpResponse<AuthUserDataBean>> flowable = mRetrofitHelper.fetchAuthLogin(authCode, phone, deviceId);
        MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>> observer = new MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthUserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        SPUtil.setToken(data.getData().getToken());
                        SPUtil.setPhone(data.getData().getPhone());
                        getView().showAuthLoginSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
