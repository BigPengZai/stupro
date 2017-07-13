package com.onlyhiedu.mobile.UI.Emc.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.AddContactContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/12.
 */

public class AddContactPresenter extends RxPresenter<AddContactContract.View> implements AddContactContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public AddContactPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void queryIMUserInfo(String phone) {
        Flowable<onlyHttpResponse<IMUserInfo>> flowable = mRetrofitHelper.fetchQueryIMUserInfo(phone);

        MyResourceSubscriber<onlyHttpResponse<IMUserInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<IMUserInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<IMUserInfo> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showIMUserInfo(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
