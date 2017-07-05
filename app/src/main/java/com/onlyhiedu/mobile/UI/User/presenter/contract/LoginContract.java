package com.onlyhiedu.mobile.UI.User.presenter.contract;

import android.content.Context;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface LoginContract {

    interface View extends BaseView {
        void showUser();
        void setPush();
        void isShowBingActivity(String token, SHARE_MEDIA share_media, String uid);
    }

    interface Presenter extends BasePresenter<LoginContract.View> {
        void getUser(String phone, String pwd,String deviceid);

        void emcLogin(String currentUsername, String currentPassword, Context context);

        void isBindUser(SHARE_MEDIA share_media, String uid, String openid,String name,String gender,String iconurl,String city,String province,String country);
    }

}
