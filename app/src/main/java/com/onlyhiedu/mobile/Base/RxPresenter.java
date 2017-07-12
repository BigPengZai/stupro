package com.onlyhiedu.mobile.Base;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.db.DemoDBManager;

import java.lang.ref.WeakReference;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuwc on 2016/11/24.
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {


    protected WeakReference<T> mView;

    CompositeDisposable mDisposables;


    @Override
    public void attachView(T view) {
        mView = new WeakReference<T>(view);
    }

    @Override
    public void detachView() {
        mView.clear();
        mView = null;
        dispose();
    }

    public T getView() {
        if (mView != null) {
            return mView.get();
        }
        return null;
    }

    //取消所有的订阅
    public void dispose() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }

    protected void addSubscription(Disposable disposable) {
        if (disposable == null) return;
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }

    protected  <V extends IMBaseView>void emcRegister(RetrofitHelper mRetrofitHelper,V v) {
        String pw = Encrypt.SHA512(SPUtil.getPhone() + "&" + "123456" + ":onlyhi");
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchEmcRegister(SPUtil.getEmcRegName(), pw);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        emcLogin(v);
                    } else {
                        Log.d(Constants.Async, "RxPresenter - emcRegister :" + data.getMessage());
                        v.showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    protected <V extends IMBaseView> void emcLogin(V v) {
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

                v.showUser();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                v.IMLoginFailure("登录失败:" + message);
            }
        });
    }

}
