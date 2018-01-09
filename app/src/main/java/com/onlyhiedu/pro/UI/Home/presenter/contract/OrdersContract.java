package com.onlyhiedu.pro.UI.Home.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.OrderList;

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
