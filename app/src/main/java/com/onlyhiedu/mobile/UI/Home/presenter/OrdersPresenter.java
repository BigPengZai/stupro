package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.OrderList;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.OrdersContract;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by pengpeng on 2017/7/31.
 */

public class OrdersPresenter extends RxPresenter<OrdersContract.View> implements OrdersContract.Presenter {


    private RetrofitHelper mRetrofitHelper;
    private int mPage = 1;

    @Inject
    public OrdersPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getOrderList(boolean isRefresh, String payStatus) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        Flowable<onlyHttpResponse<OrderList>> flowable = mRetrofitHelper.fetchGetOrderList(payStatus, mPage);

        ResourceSubscriber<onlyHttpResponse<OrderList>> observer = new ResourceSubscriber<onlyHttpResponse<OrderList>>() {

            @Override
            public void onNext(onlyHttpResponse<OrderList> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().getOrderListSuccess(data.getData().list, isRefresh);
                    } else {
                        getView().getOrderListFailure();
                        getView().showError(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                if (getView() != null) getView().showNetWorkError();
            }

            @Override
            public void onComplete() {
            }

        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }
}
