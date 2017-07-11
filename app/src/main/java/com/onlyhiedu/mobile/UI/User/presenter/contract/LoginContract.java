package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.IMBaseView;

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
