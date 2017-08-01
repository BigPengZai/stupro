package com.onlyhiedu.mobile.UI.Home.activity;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.presenter.OrdersPresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.OrdersContract;

/**
 * Created by pengpeng on 2017/7/31.
 * 我的订单
 */

public class MineOrdersActivity extends BaseActivity<OrdersPresenter> implements OrdersContract.View  {


        @Override
        protected void initInject() {
            getActivityComponent().inject(this);
        }

        @Override
        protected int getLayout() {
            return R.layout.activity_orders;
        }

        @Override
        protected void initView() {
            super.initView();

            setToolBar("我的订单");
        }

        @Override
        protected void initData() {
            super.initData();
        }

        @Override
        public void showError(String msg) {

        }
}
