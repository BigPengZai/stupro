package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public  interface NewFriendsMsgContract {

    interface View extends BaseView {
        void getNewFriendsSuccess(IMAllUserInfo data);
    }

    interface Presenter extends BasePresenter<NewFriendsMsgContract.View> {
        void getNewFriends(List<String> IMNames);
    }
}
