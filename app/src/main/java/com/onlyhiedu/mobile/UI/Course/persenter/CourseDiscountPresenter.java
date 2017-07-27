package com.onlyhiedu.mobile.UI.Course.persenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CourseDiscountContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by pengpeng on 2017/7/26.
 */

public class CourseDiscountPresenter extends RxPresenter<CourseDiscountContract.View> implements CourseDiscountContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public CourseDiscountPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getCoursePriceTypeListInfo(String type) {
        Flowable<onlyHttpResponse<List<CoursePriceTypeInfo>>> flowable = mRetrofitHelper.fetchGetCorsePriceList(type);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<List<CoursePriceTypeInfo>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CoursePriceTypeInfo>> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showPriceTypeListSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getCoursePriceList(String ctivityType, String type) {
        Flowable<onlyHttpResponse<List<CoursePriceList>>> flowable = mRetrofitHelper.fetchGetPriceList(ctivityType,type);
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<List<CoursePriceList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CoursePriceList>> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showCoursePriceList(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
