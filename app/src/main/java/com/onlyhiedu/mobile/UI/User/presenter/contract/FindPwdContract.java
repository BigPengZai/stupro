package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface FindPwdContract {

    interface View extends BaseView {

        void showSecond(int second);
        void showAuthSuccess(AuthCodeInfo info);

        void showRetrievePwd();
    }

    interface Presenter extends BasePresenter<View> {

        void readSecond();
        void getAuthCode(String phone);

        void retrievePwd(String phone,String pwd,String authcode);
    }
}
