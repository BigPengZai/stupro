package io.agore.openvcall.ui;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.board.NotifyWhiteboardOperator;
import com.onlyhiedu.mobile.Model.bean.board.ResponseWhiteboardList;
import com.onlyhiedu.mobile.Widget.MyScrollView;
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
        void setDrawableStyle(DrawView drawView, NotifyWhiteboardOperator data);

        NotifyWhiteboardOperator getNotifyWhiteboard(String str);

        int getActionType(NotifyWhiteboardOperator bean);

        void drawPoint(DrawView view, NotifyWhiteboardOperator json);

        void ScrollDrawView(MyScrollView view , NotifyWhiteboardOperator yAxis);

        void drawEraser(DrawView view, NotifyWhiteboardOperator json);

        void uploadClassConsumption(String courseUuid, String endTime);
    }
}
