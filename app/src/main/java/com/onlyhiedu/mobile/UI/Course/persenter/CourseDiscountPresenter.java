package com.onlyhiedu.mobile.UI.Course.persenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.UI.Course.persenter.contract.CourseDiscountContract;

import javax.inject.Inject;

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
    public void getInfo() {

    }
}
