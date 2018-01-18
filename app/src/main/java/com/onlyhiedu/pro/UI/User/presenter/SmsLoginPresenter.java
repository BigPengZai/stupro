package com.onlyhiedu.pro.UI.User.presenter;

import android.util.Log;

import com.onlyhiedu.pro.App.Constants;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.AuthUserDataBean;
import com.onlyhiedu.pro.Model.bean.UikitDate;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.User.presenter.contract.SmsLoginContract;
import com.onlyhiedu.pro.Utils.SPUtil;

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

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchAuthCode(phone);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showAuthSuccess();
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

                        String emcRegName = data.getData().userUuid.contains("-") ? data.getData().userUuid.replaceAll("-", "") : data.getData().userUuid;
                        Log.d(Constants.TAG, "Token : " + data.getData().token);
                        SPUtil.setUserInfo(emcRegName, data.getData().token, data.getData().phone, data.getData().userName, data.getData().avatarUrl,data.getData().agoroUid);
                            getView().showUser();
//                        if (!data.getData().registerIMFlag) {
//                            emcRegister(mRetrofitHelper,getView());
//                        } else {
//                            emcLogin(getView());
//                        }
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void registerUikit() {
        Flowable<onlyHttpResponse<UikitDate>> flowable = mRetrofitHelper.fetchregisterUikit();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<UikitDate>>() {
            @Override
            public void onNextData(onlyHttpResponse<UikitDate> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        SPUtil.setUikitAccid(data.getData().getNeteaseAccid());
                        SPUtil.setUikitToken(data.getData().getNeteaseToken());
                        getView().getUikitDate();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    /*
    * 推送
    * */

    public void setPushToken(String device_token,String tag) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchPushToken(device_token,tag);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().setPush();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }



}
