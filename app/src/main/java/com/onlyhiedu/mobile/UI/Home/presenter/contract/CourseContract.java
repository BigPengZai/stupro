package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CourseList;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public interface CourseContract  {

    interface View extends BaseView {

        void showCourseListSuccess(List<CourseList.ListBean> data);

        void showCourseListFailure();

        void showNetWorkError();
    }

    interface Presenter extends BasePresenter<CourseContract.View> {

        void getCourseList(boolean isRefresh);

        void getEndCourseList(boolean isRefresh);

        void getRoomInfoList();
    }
}
