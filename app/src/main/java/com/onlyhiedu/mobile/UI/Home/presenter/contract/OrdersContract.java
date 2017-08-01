package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.OrderList;

import java.util.List;

/**
 * Created by pengpeng on 2017/7/31.
 */

public interface OrdersContract {

    interface View extends BaseView {
        void getOrderListSuccess(List<OrderList.ListBean> data, boolean isRefresh);
        void getOrderListFailure();

        void showNetWorkError();
    }

    interface Presenter extends BasePresenter<OrdersContract.View> {
        void getOrderList(boolean isRefresh,String payStatus);
    }
}
