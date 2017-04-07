package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.RegisterInfo;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface RegContract {

    interface View extends BaseView {

        void showSecond(int second);
        void showSuccess(RegisterInfo info);

        void showAuthSuccess(AuthCodeInfo info);
    }

    interface Presenter extends BasePresenter<RegContract.View> {

        void readSecond();
        void registerUser(String userName,String phone, String pwd);

        void getAuthCode(String phone);
    }
}
