package com.onlyhiedu.pro.UI.Setting.presenter;

import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.Setting.presenter.contract.ModifyPwContract;
import com.onlyhiedu.pro.Utils.Encrypt;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.UIUtils;

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
                if (getView() != null) {
                    if (!data.isHasError()) {
                        getView().showUpdate(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

}
