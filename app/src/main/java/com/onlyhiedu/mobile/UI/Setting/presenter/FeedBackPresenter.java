package com.onlyhiedu.mobile.UI.Setting.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.FeedBackContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by pengpeng on 2017/3/29.
 */

public class FeedBackPresenter extends RxPresenter<FeedBackContract.View> implements FeedBackContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public FeedBackPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void sendFeedBack(String content) {


        Flowable<onlyHttpResponse<FeedBackInfo>> flowable = mRetrofitHelper.fetchRequestFeedBackInfo(content);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showFeedBackSuccess(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }


            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
