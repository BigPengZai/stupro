package com.onlyhiedu.pro.UI.Setting.presenter;

import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.FeedBackInfo;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.Setting.presenter.contract.FeedBackContract;

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
