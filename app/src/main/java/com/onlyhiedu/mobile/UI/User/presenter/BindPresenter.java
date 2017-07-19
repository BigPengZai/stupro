package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.BindContract;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/4.
 */

public class BindPresenter extends RxPresenter<BindContract.View> implements BindContract.Presenter{


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public BindPresenter(RetrofitHelper mRetrofitHelper) {
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
                        getView().getAuthCodeSuccess(data.getData().getAuthCode());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void bindUser(SHARE_MEDIA share_media, String uid, String phone, String name,String code) {

        Flowable<onlyHttpResponse<AuthUserDataBean>> flowable = mRetrofitHelper.fetchBindUser(share_media, uid, phone, name, code,StringUtils.getDeviceId(App.getInstance().getApplicationContext()));

        MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>> observer = new MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthUserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        String emcRegName = data.getData().userUuid.contains("-") ? data.getData().userUuid.replaceAll("-", "") : data.getData().userUuid;

                        SPUtil.setUserInfo(emcRegName,data.getData().token,data.getData().getPhone(),data.getData().userName,data.getData().avatarUrl);

                        if (!data.getData().registerIMFlag) {
                            emcRegister(mRetrofitHelper,getView());
                        } else {
                            getView().showUser();
                        }
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
