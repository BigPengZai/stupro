package com.onlyhiedu.pro.UI.User.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.IMBaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface BindContract {

    interface View extends IMBaseView {

        void showUser();

        void getAuthCodeSuccess();

        void showSecond(int i);

    }

    interface Presenter extends BasePresenter<View> {
        void getAuthCode(String phone);

        void readSecond();

        void bindUser(SHARE_MEDIA share_media, String uid, String phone, String name, String code);
    }
}
