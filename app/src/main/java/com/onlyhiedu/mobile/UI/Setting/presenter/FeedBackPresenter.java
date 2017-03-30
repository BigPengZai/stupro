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

    private  RetrofitHelper mRetrofiHelper;

    @Inject
    public FeedBackPresenter(RetrofitHelper mRetrofiHelper) {
        this.mRetrofiHelper = mRetrofiHelper;
    }

    @Override
    public void sendFeedBack(String content) {


        Flowable<onlyHttpResponse<FeedBackInfo>> flowable = mRetrofiHelper.fetchRequestFeedBackInfo(content);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {
                    getView().showFeedBackSuccess(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofiHelper.startObservable(flowable,observer));
    }
}
