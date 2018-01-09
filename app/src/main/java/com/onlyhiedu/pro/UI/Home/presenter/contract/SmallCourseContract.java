package com.onlyhiedu.pro.UI.Home.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.CourseList;
import com.onlyhiedu.pro.Model.bean.RoomInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public interface SmallCourseContract {

    interface View extends BaseView {

        void showCourseListSuccess(List<CourseList.ListBean> data, boolean isRefresh);

        void showCourseListFailure();

        void showNetWorkError();

        void showRoomInfoSucess(RoomInfo roomInfo);
    }

    interface Presenter extends BasePresenter<SmallCourseContract.View> {

        void getCourseList(boolean isRefresh);

        void getEndCourseList(boolean isRefresh);

        void getRoomInfoList(String uuid);
    }
}
