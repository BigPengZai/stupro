package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.hyphenate.easeui.domain.EaseUser;
import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by Administrator on 2017/7/6.
 */

public interface ContactListContract {

    interface View extends BaseView {
        void deleteFriendSuccess(EaseUser user);
    }

    interface Presenter extends BasePresenter<ContactListContract.View> {
        void deleteFriends(EaseUser user);
    }
}
