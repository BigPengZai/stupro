package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.IMBaseView;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface RegContract {

    interface View extends IMBaseView {

        void showSecond(int second);


        void showAuthSuccess(int authCode);

        void showRegState(boolean isReg);


        void IMLoginFailure(String s);
        void showUser();
        void setPush();
    }

    interface Presenter extends BasePresenter<RegContract.View> {

        void readSecond();

        void registerUser(String userName, String phone, String pwd, String authcode);

        void getAuthCode(String phone);

        void isRegister(String phone);

        void getUser(String phone, String pwd,String deviceid);

    }
}
