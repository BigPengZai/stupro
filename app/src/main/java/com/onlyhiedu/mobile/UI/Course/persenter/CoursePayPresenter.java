package com.onlyhiedu.mobile.UI.Course.persenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CoursePayContract;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by pengpeng on 2017/7/27.
 */

public class CoursePayPresenter extends RxPresenter<CoursePayContract.View> implements CoursePayContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public CoursePayPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getPayMoney(String coursePriceUuid, String code) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchGetPayMoney(coursePriceUuid, code);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showGetPaySucess((double) data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getPingppPaymentByJson(String coursePriceUuid, String channel) {
        Flowable<onlyHttpResponse<PingPaySucessInfo>> flowable = mRetrofitHelper.fetchGetPingPay(coursePriceUuid, channel);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<PingPaySucessInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<PingPaySucessInfo> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showPingPaySucess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }

    @Override
    public void getBaiduPay(String uuid) {
        Flowable<onlyHttpResponse<String>> flowable = mRetrofitHelper.fetchGetBaiduPay(uuid);
        ResourceSubscriber observer = new ResourceSubscriber<onlyHttpResponse<String>>() {
            @Override
            public void onNext(onlyHttpResponse<String> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().getBaiduPaySuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                if (getView() != null) {
                    getView().getBaiduPayFailure();
                }
            }

            @Override
            public void onComplete() {

            }

        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
