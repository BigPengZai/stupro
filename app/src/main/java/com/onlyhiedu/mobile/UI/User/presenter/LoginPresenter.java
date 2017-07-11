package com.onlyhiedu.mobile.UI.User.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;
import com.onlyhiedu.mobile.UI.User.presenter.contract.LoginContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.db.DemoDBManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    public static final String TAG = LoginPresenter.class.getSimpleName();

    @Inject
    public LoginPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getUser(String phone, String pwd, String deviceid) {
        long timeMillis = System.currentTimeMillis();
        String password = Encrypt.SHA512(UIUtils.sha512(phone, pwd) + timeMillis);
        Flowable<onlyHttpResponse<UserDataBean>> flowable = mRetrofitHelper.fetchUser(phone, password, timeMillis, deviceid);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<UserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<UserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        String emcRegName = data.getData().userUuid.contains("-") ? data.getData().userUuid.replaceAll("-", "") : data.getData().userUuid;
                        SPUtil.setEmcRegName(emcRegName);
                        Log.d(Constants.TAG, "Token : " + data.getData().token);
                        SPUtil.setToken(data.getData().token);
                        SPUtil.setPhone(data.getData().phone);
                        SPUtil.setName(data.getData().userName);
                        if (!data.getData().registerIMFlag) {
                            emcRegister();
                        } else {
                            emcLogin();
                        }
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    /*
    * 推送
    * */

    public void setPushToken(String device_token, String tag) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchPushToken(device_token, tag);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().setPush();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    /**
     * 环信登录
     */
    @Override
    public void emcLogin() {
        String pwd = Encrypt.SHA512(SPUtil.getPhone() + "&" + "123456" + ":onlyhi");
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(SPUtil.getName());
        EMClient.getInstance().login(SPUtil.getEmcRegName(), pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        App.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                getView().showUser();

            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                getView().IMLoginFailure("登录失败:" + message);
            }
        });
    }

    /**
     * 环信注册
     */
    @Override
    public void emcRegister() {
        String pw = Encrypt.SHA512(SPUtil.getPhone() + "&" + "123456" + ":onlyhi");
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchEmcRegister(SPUtil.getEmcRegName(), pw);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        emcLogin();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void isBindUser(SHARE_MEDIA share_media, String uid, String openid, String name, String gender, String iconurl, String city, String province, String country) {


        Flowable<onlyHttpResponse<AuthUserDataBean>> flowable = mRetrofitHelper.fetchIsBindUser(share_media, uid, openid, name, gender, iconurl, city, province, country, StringUtils.getDeviceId(App.getInstance()));

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthUserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().isShowBingActivity(data.getData().token, data.getData().phone, data.getData().userName, share_media, uid);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

}
