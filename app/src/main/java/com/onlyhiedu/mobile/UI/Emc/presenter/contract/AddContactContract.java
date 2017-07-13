package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface AddContactContract {

    interface View extends BaseView {
        void showIMUserInfo(IMUserInfo info);
    }

    interface Presenter extends BasePresenter<AddContactContract.View> {
        void queryIMUserInfo(String phone);
    }
}
