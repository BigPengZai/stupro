package com.onlyhiedu.mobile.UI.User.presenter.contract;

import android.content.Context;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface LoginContract {

    interface View extends BaseView {
        void showUser();
        void setPush();

    }

    interface Presenter extends BasePresenter<LoginContract.View> {
        void getUser(String phone, String pwd,String deviceid);

        void emcLogin(String currentUsername, String currentPassword, Context context);
    }

}
