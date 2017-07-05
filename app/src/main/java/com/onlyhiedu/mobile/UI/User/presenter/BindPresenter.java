package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserIsRegister;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.BindContract;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
    public void getRegState(String phone) {
        Flowable<onlyHttpResponse<UserIsRegister>> flowable = mRetrofitHelper.fetchIsReg(phone);
        MyResourceSubscriber<onlyHttpResponse<UserIsRegister>> observer = new MyResourceSubscriber<onlyHttpResponse<UserIsRegister>>() {
            @Override
            public void onNextData(onlyHttpResponse<UserIsRegister> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showRegState(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void bindUser(SHARE_MEDIA share_media, String uid, String phone, String name) {

        Flowable<onlyHttpResponse<AuthUserDataBean>> flowable = mRetrofitHelper.fetchBindUser(share_media, uid, phone, name, StringUtils.getDeviceId(App.getInstance().getApplicationContext()));

        MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>> observer = new MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthUserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        SPUtil.setToken(data.getData().token);
                        SPUtil.setPhone(data.getData().phone);
                        getView().showBindUser(data.getData());
                        getView().showError(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }


}
