package com.onlyhiedu.mobile.Base;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
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
    //获得学生信息
    protected <V extends IMBaseView>void getStuInfo(RetrofitHelper mRetrofitHelper,V v) {
        Flowable<onlyHttpResponse<StudentInfo>> flowable = mRetrofitHelper.fetchStudentInfo();

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<StudentInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<StudentInfo> data) {
                if (getView() != null ) {
                    if (!data.isHasError()) {
                        SPUtil.setAvatarUrl(data.getData().iconurl);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    //注册环信
    protected  <V extends IMBaseView>void emcRegister(RetrofitHelper mRetrofitHelper,V v) {
        String pwd = Encrypt.SHA512(SPUtil.getEmcRegName() + "&" + "123456" + ":onlyhi");
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchEmcRegister(SPUtil.getEmcRegName(), pwd);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        v.showUser();
                    } else {
                        Log.d(Constants.Async, "RxPresenter - emcRegister :" + data.getMessage());
                        v.showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

}
