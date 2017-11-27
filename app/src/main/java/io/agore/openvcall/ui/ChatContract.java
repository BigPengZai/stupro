package io.agore.openvcall.ui;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public interface ChatContract {


    interface View extends BaseView {


        //流统计 Flow statistics
        void showFlowStatistics();

        void showCourseWareImageList(List<CourseWareImageList> data, int pageNum, boolean restart);

        //下课
        void showUpdateEndTime(String msg);
    }

    interface Presenter extends BasePresenter<ChatContract.View> {
        //上传流统计
        void uploadStatistics(String classTime, String courseUuid);

        //课件图片列表
        void getCourseWareImageList(String wareId, int pageNum, boolean restart);

        //下课
        void getUpdateEndTime(String courseUuid);
    }
}
