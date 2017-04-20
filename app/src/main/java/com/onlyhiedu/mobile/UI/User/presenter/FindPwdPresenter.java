package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.FindPwdContract;
import com.onlyhiedu.mobile.Utils.StringUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/3.
 */

public class FindPwdPresenter extends RxPresenter<FindPwdContract.View> implements FindPwdContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public FindPwdPresenter(RetrofitHelper mRetrofitHelper) {
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

    @Override
    public void retrievePwd(String phone, String pwd, String authCode) {

        String deviceId = StringUtils.getDeviceId();

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchRetrieve(phone, pwd, authCode, deviceId);

        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null)
                    getView().showError(data.getMessage());
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
