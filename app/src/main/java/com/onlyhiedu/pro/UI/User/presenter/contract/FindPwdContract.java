package com.onlyhiedu.pro.UI.User.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.AuthCodeInfo;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface FindPwdContract {

    interface View extends BaseView {

        void showSecond(int second);
        void showAuthSuccess(AuthCodeInfo info);

    }

    interface Presenter extends BasePresenter<View> {

        void readSecond();
        void getAuthCode(String phone);

        void retrievePwd(String phone,String pwd,String authcode);
    }
}
