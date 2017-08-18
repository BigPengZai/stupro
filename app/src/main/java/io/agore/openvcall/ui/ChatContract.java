package io.agore.openvcall.ui;

import android.app.Activity;
import android.widget.ImageView;

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

        void showCourseWareImageList(List<CourseWareImageList> data,int page);

        //流统计 Flow statistics
        void showFlowStatistics();

        void showCourseWareImageList(List<CourseWareImageList> data);
    }

    interface Presenter extends BasePresenter<ChatContract.View> {
        //上传流统计
        void uploadStatistics(String classTime,String courseUuid);
        void getCourseWareImageList(String wareId ,int page);
        //课件图片列表
        void getCourseWareImageList(String wareId );
        void setDrawableStyle(DrawView drawView, ResponseWhiteboardList data, ImageView courseWareImage);
        void setDrawableStyle(DrawView drawView, NotifyWhiteboardOperator data);
        void setBoardCreate(ImageView courseWareImage,DrawView drawView, NotifyWhiteboardOperator data);

        NotifyWhiteboardOperator getNotifyWhiteboard(String str);

        int getActionType(NotifyWhiteboardOperator bean);

        void drawPoint(DrawView view, NotifyWhiteboardOperator json);

        void ScrollDrawView(Activity activity, MyScrollView view , NotifyWhiteboardOperator yAxis);

        void drawEraser(DrawView view, NotifyWhiteboardOperator json);

        void drawEraserRect(DrawView view, NotifyWhiteboardOperator json);

        void drawOval(DrawView view, NotifyWhiteboardOperator json);

        void drawRectangle(DrawView view, NotifyWhiteboardOperator json);

        void drawText(DrawView view, NotifyWhiteboardOperator json);

    }
}
