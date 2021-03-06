package com.onlyhiedu.pro.UI.Course.persenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.PingPayStatus;
import com.onlyhiedu.pro.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.pro.Model.bean.PingPaySucessInfoAliPay;
import com.onlyhiedu.pro.Model.bean.StudentInfo;

/**
 * Created by pengpeng on 2017/7/27.
 */

public interface CoursePayContract {
    interface View extends BaseView {
        void showGetPaySucess(double data);

        void showPingPaySucess(PingPaySucessInfo info);

        void showPingPaySucessAliPay(PingPaySucessInfoAliPay info);
        void getBaiduPaySuccess(String url);
        void getBaiduPayFailure();

        void getPingPayStatusSucess(PingPayStatus data);


        void showStudentInfo(StudentInfo info);
    }

    interface Presenter extends BasePresenter<CoursePayContract.View> {
        void getPayMoney(String coursePriceUuid, String code);

        void getPingppPaymentByJson(String coursePriceUuid, String channel,String code);
        void getPingppPaymentByJsonAlipay(String coursePriceUuid, String channel,String code);

        void getBaiduPay(String uuid,String code,String name,String phone);
        void updateGrade(String grade);

        void updateSubject(String subject);

        void getPingPayStatus(String id);
        void getStudentInfo();

        void getOrderPingppPayment(String coursePriceUuid, String channel,String code);
        void getOrderPingppPaymentAlipay(String coursePriceUuid, String channel,String code);

        void getOrderBaiduPay(String uuid,String code);
    }
}
