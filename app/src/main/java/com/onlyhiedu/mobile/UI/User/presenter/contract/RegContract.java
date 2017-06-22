package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface RegContract {

    interface View extends BaseView {

        void showSecond(int second);
        void showSuccess(String info);

        void showAuthSuccess(int authCode);

        void showRegState(boolean isReg);
    }

    interface Presenter extends BasePresenter<RegContract.View> {

        void readSecond();
        void registerUser(String userName,String phone, String pwd,String authcode);

        void getAuthCode(String phone);

        void isRegister(String phone);
    }
}
