package com.onlyhiedu.pro.UI.User.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.App.Constants;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.UserDataBean;
import com.onlyhiedu.pro.Model.bean.UserIsRegister;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.User.presenter.contract.RegContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.StringUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RegPresenter extends RxPresenter<RegContract.View> implements RegContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public RegPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void readSecond() {
        Flowable<Long> flowable = Flowable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS);

        MyResourceSubscriber observer = new MyResourceSubscriber<Long>() {
            @Override
            public void onNextData(Long value) {
                if (getView() != null)
                    getView().showSecond(60 - new Long(value).intValue());
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));


    }


    @Override
    public void registerUser(String userName, String phone, String pwd, String authcode) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchRegisterInfo(userName, phone,pwd, authcode);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getUser(phone,pwd,StringUtils.getDeviceId(App.getInstance().getApplicationContext()));
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getAuthCode(String phone) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchAuthCode(phone);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showAuthSuccess();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    public boolean confirmThird(String code, String name) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "请填写验证码信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(App.getInstance().getApplicationContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        return StringUtils.checkAccountMark(name);
    }

    /**
     * 号码是否注册
     */
    @Override
    public void isRegister(String phone) {
        Flowable<onlyHttpResponse<UserIsRegister>> flowable = mRetrofitHelper.fetchIsReg(phone);
        MyResourceSubscriber<onlyHttpResponse<UserIsRegister>> observer = new MyResourceSubscriber<onlyHttpResponse<UserIsRegister>>() {
            @Override
            public void onNextData(onlyHttpResponse<UserIsRegister> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showRegState(data.getData().registerFlag);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    @Override
    public void getUser(String phone, String pwd, String deviceid) {
        long timeMillis = System.currentTimeMillis();
        String password = Encrypt.SHA512(pwd + timeMillis);
        Flowable<onlyHttpResponse<UserDataBean>> flowable = mRetrofitHelper.fetchUser(phone, password, timeMillis, deviceid);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<UserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<UserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        String emcRegName = data.getData().userUuid.contains("-") ? data.getData().userUuid.replaceAll("-", "") : data.getData().userUuid;
                        Log.d(Constants.TAG, "Token : " + data.getData().token);
                        SPUtil.setUserInfo(emcRegName,data.getData().token,data.getData().phone,data.getData().userName,data.getData().avatarUrl,data.getData().agoraUid);
//                        emcRegister(mRetrofitHelper,getView());
                        getView().showUser();
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


//    /**
//     * 环信登录
//     */
//    @Override
//    public void emcLogin() {
//        String pwd = Encrypt.SHA512(SPUtil.getPhone() + "&" + "123456" + ":onlyhi");
//        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
//        // close it before login to make sure DemoDB not overlap
//        DemoDBManager.getInstance().closeDB();
//        // reset current user name before login
//        DemoHelper.getInstance().setCurrentUserName(SPUtil.getName());
//        EMClient.getInstance().login(SPUtil.getEmcRegName(), pwd, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                // ** manually load all local groups and conversation
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                // update current user's display name for APNs
//                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
//                        App.currentUserNick.trim());
//                if (!updatenick) {
//                    Log.e("LoginActivity", "update current user nick fail");
//                }
//                // get user's info (this should be get from App's server or 3rd party service)
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//
//                getView().showUser();
//
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//            @Override
//            public void onError(final int code, final String message) {
//                getView().IMLoginFailure("登录失败:" + message);
//            }
//        });
//    }
//
//    /**
//     * 环信注册
//     */
//    @Override
//    public void emcRegister() {
//        String pw = Encrypt.SHA512(SPUtil.getPhone() + "&" + "123456" + ":onlyhi");
//        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchEmcRegister(SPUtil.getEmcRegName(), pw);
//        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
//            @Override
//            public void onNextData(onlyHttpResponse data) {
//                if (getView() != null && data != null) {
//                    if (!data.isHasError()) {
//                        emcLogin();
//                    } else {
//                        getView().showError(data.getMessage());
//                    }
//                }
//            }
//        };
//        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
//    }


}
