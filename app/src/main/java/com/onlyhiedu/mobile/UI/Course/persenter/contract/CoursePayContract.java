package com.onlyhiedu.mobile.UI.Course.persenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.PingPayStatus;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;

/**
 * Created by pengpeng on 2017/7/27.
 */

public interface CoursePayContract {
    interface View extends BaseView {
        void showGetPaySucess(double data);

        void showPingPaySucess(PingPaySucessInfo info);

        void getBaiduPaySuccess(String url);
        void getBaiduPayFailure();

        void getPingPayStatus(PingPayStatus data);

        void getGradeSubject(int code);
    }

    interface Presenter extends BasePresenter<CoursePayContract.View> {
        void getPayMoney(String coursePriceUuid, String code);

        void getPingppPaymentByJson(String coursePriceUuid, String channel);

        void getBaiduPay(String uuid);
        void updateGrade(String grade);

        void updateSubject(String subject);

        void getPingPayStatus(String id);

        void isEmptyGradeSubject();
    }
}
