package com.onlyhiedu.mobile.UI.Course.persenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;

/**
 * Created by pengpeng on 2017/7/26.
 */

public interface CourseDiscountContract {
    interface View extends BaseView {
        void showInfoSuccess();
    }

    interface Presenter extends BasePresenter<CourseDiscountContract.View> {
        void getInfo();
    }
}