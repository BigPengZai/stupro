package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by Administrator on 2017/7/6.
 */

public interface ContactListContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<ContactListContract.View> {
        void deleteFriends(String phone);
    }
}
