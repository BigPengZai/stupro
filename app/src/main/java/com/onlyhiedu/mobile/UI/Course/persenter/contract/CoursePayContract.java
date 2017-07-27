package com.onlyhiedu.mobile.UI.Course.persenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;

import java.util.List;

/**
 * Created by pengpeng on 2017/7/27.
 */

public interface CoursePayContract {
    interface View extends BaseView {
        void showGetPaySucess(double data);

        void showPingPaySucess(PingPaySucessInfo info);
    }

    interface Presenter extends BasePresenter<CoursePayContract.View> {
        void getPayMoney(String coursePriceUuid,String code);

        void getPingppPaymentByJson(String coursePriceUuid,String channel);
    }
}
