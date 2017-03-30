package io.agore.openvcall.ui;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.board.ResponseWhiteboardList;
import com.onlyhiedu.mobile.Widget.draw.DrawView;

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

        void setDrawableStyle(DrawView drawView, ResponseWhiteboardList data);

        int getActionType(String msg);

        List<String[]> parseDrawJson(String msg);

        void drawPoint(DrawView view,List<String[]> data);

        void uploadClassConsumption(String courseUuid,String endTime);
    }
}
