package com.onlyhiedu.mobile.UI.Course.persenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.PingPayStatus;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfoAliPay;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
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
    //订单 Ping++ 支付 wx
    @Override
    public void getOrderPingppPayment(String coursePriceUuid, String channel,String code) {
        Flowable<onlyHttpResponse<PingPaySucessInfo>> flowable = mRetrofitHelper.fetchOrderGetPingPay(coursePriceUuid, channel,code);
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
    //订单 Ping++ 支付 alipay
    @Override
    public void getOrderPingppPaymentAlipay(String coursePriceUuid, String channel,String code) {
        Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> flowable = mRetrofitHelper.fetchOrderGetPingPayAliPay(coursePriceUuid, channel,code);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<PingPaySucessInfoAliPay>>() {
            @Override
            public void onNextData(onlyHttpResponse<PingPaySucessInfoAliPay> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showPingPaySucessAliPay(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }



    //订单 百度支付
    @Override
    public void getOrderBaiduPay(String uuid,String code) {
        Flowable<onlyHttpResponse<String>> flowable = mRetrofitHelper.fetchOrderGetBaiduPay(uuid,code);
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

    //课时包 Ping++ 支付 wx
    @Override
    public void getPingppPaymentByJson(String coursePriceUuid, String channel,String code) {
        Flowable<onlyHttpResponse<PingPaySucessInfo>> flowable = mRetrofitHelper.fetchGetPingPay(coursePriceUuid, channel,code);
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

    //课时包 Ping++ 支付  alipay
    @Override
    public void getPingppPaymentByJsonAlipay(String coursePriceUuid, String channel,String code) {
        Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> flowable = mRetrofitHelper.fetchGetPingPayAliPay(coursePriceUuid, channel,code);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<PingPaySucessInfoAliPay>>() {
            @Override
            public void onNextData(onlyHttpResponse<PingPaySucessInfoAliPay> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showPingPaySucessAliPay(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }



    //课时包百度支付
    @Override
    public void getBaiduPay(String uuid,String code,String name,String phone ) {
        Flowable<onlyHttpResponse<String>> flowable = mRetrofitHelper.fetchGetBaiduPay(uuid,code,name,phone);
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








    @Override
    public void updateGrade(String grade) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateGrade(grade);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {

                    getView().showError(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void updateSubject(String subject) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateSubject(subject);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {
                    getView().showError(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    //课时包 PIng++ 订单支付状态查询
    @Override
    public void getPingPayStatus(String id) {
        Flowable<onlyHttpResponse<PingPayStatus>> flowable = mRetrofitHelper.fetchPingPayStatus(id);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<PingPayStatus>>() {
            @Override
            public void onNextData(onlyHttpResponse<PingPayStatus> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().getPingPayStatusSucess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    @Override
    public void getStudentInfo() {

        Flowable<onlyHttpResponse<StudentInfo>> flowable = mRetrofitHelper.fetchStudentInfo();

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<StudentInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<StudentInfo> data) {
                if (getView() != null ) {
                    if (!data.isHasError()) {
                        getView().showStudentInfo(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
