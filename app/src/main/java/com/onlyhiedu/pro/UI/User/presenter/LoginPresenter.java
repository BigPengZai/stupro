package com.onlyhiedu.pro.UI.User.presenter;

import android.util.Log;

import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.App.Constants;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.UikitDate;
import com.onlyhiedu.pro.Model.bean.UserDataBean;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.Model.http.registerUikit;
import com.onlyhiedu.pro.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.StringUtils;
import com.onlyhiedu.pro.Utils.UIUtils;

import javax.inject.Inject;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                        SPUtil.setUserInfo(emcRegName, data.getData().token, data.getData().phone, data.getData().userName, data.getData().avatarUrl,data.getData().agoraUid);
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

/*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://clienttest.haiketang.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        registerUikit registerUikit = retrofit.create(registerUikit.class);
        Call<UikitDate> uikitDateCall = registerUikit.registerUikit(SPUtil.getToken());
        uikitDateCall.enqueue(new Callback<UikitDate>() {
            @Override
            public void onResponse(Call<UikitDate> call, Response<UikitDate> response) {

            }

            @Override
            public void onFailure(Call<UikitDate> call, Throwable t) {
//                Log.d()
            }
        });*/
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
