package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/3/17.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public LoginPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getUser(String phone, String pwd) {

        long timeMillis = System.currentTimeMillis();
        String password = Encrypt.SHA512( UIUtils.sha512(phone, pwd) + timeMillis) ;


        Flowable<onlyHttpResponse<UserDataBean>> flowable = mRetrofitHelper.fetchUser(phone, password, timeMillis);

        ResourceSubscriber observer = new ResourceSubscriber<onlyHttpResponse<UserDataBean>>() {
            @Override
            public void onNext(onlyHttpResponse<UserDataBean> data) {

                if (getView() != null) {

                    if (data.isHasError()) {
                        SPUtil.setToken(data.getData().token);
                        getView().showUser();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
                if (getView() != null) getView().showError(Constants.NET_ERROR);
            }

            @Override
            public void onComplete() {
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
