package com.onlyhiedu.mobile.UI.Setting.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.ModifyPwContract;
import com.onlyhiedu.mobile.Utils.Encrypt;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/22.
 */

public class ModifyPwPresenter extends RxPresenter<ModifyPwContract.View> implements ModifyPwContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public ModifyPwPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void updatePassword(String oldPassword, Long timestamp, String newPassword) {

        String password = Encrypt.SHA512(UIUtils.sha512(SPUtil.getPhone(), oldPassword) + timestamp);

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdatePassword(password, timestamp, UIUtils.sha512(SPUtil.getPhone(), newPassword));

        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) getView().showUpdate(data.getMessage());
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

}
