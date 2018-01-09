package com.onlyhiedu.pro.UI.User.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.IMBaseView;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface LoginContract {

    interface View extends IMBaseView {

        void setPush();

    }

    interface Presenter extends BasePresenter<LoginContract.View> {
        void getUser(String phone, String pwd,String deviceid);
    }

}
