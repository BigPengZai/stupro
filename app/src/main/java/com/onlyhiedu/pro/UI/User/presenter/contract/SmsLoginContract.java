package com.onlyhiedu.pro.UI.User.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.IMBaseView;
import com.onlyhiedu.pro.Model.bean.AuthUserDataBean;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface SmsLoginContract {

    interface View extends IMBaseView {

        void showSecond(int second);

        void showAuthSuccess(int info);


        void showAuthLoginSuccess(AuthUserDataBean info);

        void setPush();
    }

    interface Presenter extends BasePresenter<View> {

        void readSecond();

        void getAuthCode(String phone);

        void authLogin(String authcode, String phone, String deviceid);
    }
}