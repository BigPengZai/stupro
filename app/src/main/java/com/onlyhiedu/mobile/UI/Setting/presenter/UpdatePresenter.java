package com.onlyhiedu.mobile.UI.Setting.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdateContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by pengpeng on 2017/3/24.
 */

public class UpdatePresenter extends RxPresenter<UpdateContract.View> implements UpdateContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public UpdatePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void updateVersion(boolean isMain) {
        String deviceType = "Android";
        Flowable<onlyHttpResponse<UpdateVersionInfo>> flowable = mRetrofitHelper.fetchUpdateVersion(deviceType);
        MyResourceSubscriber<onlyHttpResponse<UpdateVersionInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<UpdateVersionInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<UpdateVersionInfo> data) {
                if (getView() != null && data.getData() != null) {
                    if (!data.isHasError()) {
                        data.getData().setMain(isMain);
                        getView().showUpdateSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }

                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
