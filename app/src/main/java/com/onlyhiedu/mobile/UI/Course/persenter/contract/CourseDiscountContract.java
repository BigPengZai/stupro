package com.onlyhiedu.mobile.UI.Course.persenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;

import java.util.List;

/**
 * Created by pengpeng on 2017/7/26.
 */

public interface CourseDiscountContract {
    interface View extends BaseView {
        void showPriceTypeListSuccess(List<CoursePriceTypeInfo> data);

        void showCoursePriceList(List<CoursePriceList> data);
    }

    interface Presenter extends BasePresenter<CourseDiscountContract.View> {
        void getCoursePriceTypeListInfo(String type);

        void getCoursePriceList(String ctivityType, String type);
    }
}