package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public interface CourseContract {

    interface View extends BaseView {

        void showCourseListSuccess(List<CourseList.ListBean> data,boolean isRefresh);

        void showCourseListFailure();

        void showNetWorkError();

        void showRoomInfoSucess(RoomInfo roomInfo);
    }

    interface Presenter extends BasePresenter<CourseContract.View> {

        void getCourseList(boolean isRefresh);

        void getEndCourseList(boolean isRefresh);

        void getRoomInfoList(String uuid);
    }
}
