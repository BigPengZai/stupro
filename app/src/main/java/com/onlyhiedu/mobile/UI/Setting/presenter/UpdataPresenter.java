package com.onlyhiedu.mobile.UI.Setting.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.UpdataVersionInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdataContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by pengpeng on 2017/3/24.
 */

public class UpdataPresenter extends RxPresenter<UpdataContract.View> implements UpdataContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public UpdataPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void updataVersion() {
        Flowable<onlyHttpResponse<UpdataVersionInfo>> flowable = mRetrofitHelper.fetchUpdataVersion();

        MyResourceSubscriber<onlyHttpResponse<UpdataVersionInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<UpdataVersionInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<UpdataVersionInfo> data) {
                if (getView() != null && data.getData() != null) {
                    if (data.isHasError())
                        getView().showUpdataSuccess(data.getData());
                    else
                        getView().showError(data.getMessage());

                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
