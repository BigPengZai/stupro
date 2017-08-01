package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.OrdersContract;

import javax.inject.Inject;

/**
 * Created by pengpeng on 2017/7/31.
 */

public class OrdersPresenter extends RxPresenter<OrdersContract.View> implements OrdersContract.Presenter {



    private RetrofitHelper mRetrofitHelper;

    @Inject
    public OrdersPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


}
