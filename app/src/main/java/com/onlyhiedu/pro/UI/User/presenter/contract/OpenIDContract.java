package com.onlyhiedu.pro.UI.User.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.IMBaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface OpenIDContract {

    interface View extends IMBaseView {
        void isShowBingActivity(SHARE_MEDIA share_media, String uid);

    }

    interface Presenter extends BasePresenter<OpenIDContract.View> {
        void isBindUser(SHARE_MEDIA share_media, String uid, String openid, String name, String gender, String iconurl, String city, String province, String country);
    }

}
