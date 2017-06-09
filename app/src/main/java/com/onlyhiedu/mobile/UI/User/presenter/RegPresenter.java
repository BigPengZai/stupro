package com.onlyhiedu.mobile.UI.User.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.RegContract;
import com.onlyhiedu.mobile.Utils.StringUtils;

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
    public void registerUser(String userName, String phone, String pwd,String authcode) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchRegisterInfo(userName, phone, pwd,authcode);
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
                        getView().showAuthSuccess(data.getData().getAuthCode());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    public boolean confirmThird(String code, String name, int serviceCode) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "请填写验证码信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!code.equals(serviceCode+"")) {
            Toast.makeText(App.getInstance().getApplicationContext(), "验证码信息错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return StringUtils.checkAccountMark(name);
    }

}
