package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface SmsLoginContract {

    interface View extends BaseView {

        void showSecond(int second);
        void showAuthSuccess(AuthCodeInfo info);

        void showAuthLoginSuccess(AuthUserDataBean info);
    }

    interface Presenter extends BasePresenter<View> {

        void readSecond();
        void getAuthCode(String phone);

        void authLogin(String token,String authcode,String phone,String deviceid);
    }
}
