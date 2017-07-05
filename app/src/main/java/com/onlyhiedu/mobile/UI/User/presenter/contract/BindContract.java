package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserIsRegister;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface BindContract {

    interface View extends BaseView {
        void showRegState(UserIsRegister phone);
        void showBindUser(AuthUserDataBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getRegState(String phone);

        void bindUser(SHARE_MEDIA share_media, String uid, String phone, String name);
    }
}
