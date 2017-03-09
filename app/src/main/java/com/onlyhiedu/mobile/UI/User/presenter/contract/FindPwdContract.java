package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface FindPwdContract {

    interface View extends BaseView {

        void showSecond(int second);
    }

    interface Presenter extends BasePresenter<View> {

        void readSecond();
    }
}
