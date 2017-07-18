package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;

/**
 * Created by Administrator on 2017/7/18.
 */

public interface EaseConversationListContract {

    interface View extends BaseView {
        void getIMUserFriendListSuccess(IMAllUserInfo contacts);
    }

    interface Presenter extends BasePresenter<EaseConversationListContract.View> {
        void getIMUserFriendList();
    }
}
