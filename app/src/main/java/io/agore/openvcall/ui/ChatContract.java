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

        void showCourseWareImageList(List<CourseWareImageList> data);

        void showClassConsumption(String msg);
    }

    interface Presenter extends BasePresenter<ChatContract.View> {

        void getCourseWareImageList(String wareId);

        void uploadClassConsumption(String courseUuid,String endTime);
    }
}
