package com.onlyhiedu.mobile.UI.User.presenter;

import android.util.Log;

import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    public static final String TAG = LoginPresenter.class.getSimpleName();

    @Inject
    public LoginPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getUser(String phone, String pwd, String deviceid) {
        long timeMillis = System.currentTimeMillis();
        String password = Encrypt.SHA512(UIUtils.sha512(phone, pwd) + timeMillis);
        Flowable<onlyHttpResponse<UserDataBean>> flowable = mRetrofitHelper.fetchUser(phone, password, timeMillis, deviceid);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<UserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<UserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        String emcRegName = data.getData().userUuid.replace("-", "");
                        Log.d(Constants.TAG, "Token : " + data.getData().token);
                        SPUtil.setUserInfo(emcRegName, data.getData().token, data.getData().phone, data.getData().userName, data.getData().avatarUrl);
                        getView().showUser();

//                        if (!data.getData().registerIMFlag) {
//                            emcRegister(mRetrofitHelper, getView());
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


    /*
    * 推送
    * */
    public void setPushToken(String device_token, String tag) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchPushToken(device_token, tag);
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
