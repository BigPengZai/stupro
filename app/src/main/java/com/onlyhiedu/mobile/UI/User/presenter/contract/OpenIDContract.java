package com.onlyhiedu.mobile.UI.User.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface OpenIDContract {

    interface View extends BaseView {
        void isShowBingActivity(String token, String phone, String name, SHARE_MEDIA share_media, String uid);

    }

    interface Presenter extends BasePresenter<OpenIDContract.View> {
        void isBindUser(SHARE_MEDIA share_media, String uid, String openid, String name, String gender, String iconurl, String city, String province, String country);
    }

}
