package io.agore.openvcall.ui;

import android.graphics.Color;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.ClassConsumption;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.board.MethodType;
import com.onlyhiedu.mobile.Model.bean.board.NotifyWhiteboardOperator;
import com.onlyhiedu.mobile.Model.bean.board.ResponseWhiteboardList;
import com.onlyhiedu.mobile.Model.bean.finishclass.ResponseFinishClassData;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Widget.MyScrollView;
import com.onlyhiedu.mobile.Widget.draw.DrawView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter {


    public static final String Notify_WhiteboardOperator = "Notify_WhiteboardOperator";
    public static final int DRAW = 1;
    public static final int SET = 2;
    public static final int SCROLL = 3;
    public static final int Eraser = 4;
    public static final int Oval = 5;
    public static final int Rect = 6;
    public static final int Line = 7;
    public static final int Destory = 8;//老师离开教室
    public static final int Create = 9;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public ChatPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getCourseWareImageList(String wareId) {

        Flowable<onlyHttpResponse<List<CourseWareImageList>>> flowable = mRetrofitHelper.fetchGetCoursewareImageList(wareId);

        MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>> subscriber = new MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CourseWareImageList>> data) {
                if (getView() != null) {
                    if (!data.isHasError()) getView().showCourseWareImageList(data.getData());
                    else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }

    @Override
    public void setDrawableStyle(DrawView drawView, ResponseWhiteboardList data) {
        ResponseWhiteboardList.ResponseParamBean.WhiteboardListBean bean = data.ResponseParam.WhiteboardList.get(0);
        drawView.setFontSize(bean.WhiteboardPenSize);
        drawView.setDrawColor(Color.parseColor("#" + bean.WhiteboardPenColor));
    }

    @Override
    public void setDrawableStyle(DrawView drawView, NotifyWhiteboardOperator data) {
        String s = data.NotifyParam.MethodParam;
        drawView.setFontSize(Integer.parseInt(s.substring(s.indexOf("PenSize=") + 8, s.indexOf("|PenColor"))));
        drawView.setDrawColor(Color.parseColor("#" + s.substring(s.indexOf("PenColor=") + 9, s.indexOf("|EraserSize"))));
    }

    @Override
    public void setBoardCreate(DrawView drawView, NotifyWhiteboardOperator data) {
        String s = data.NotifyParam.MethodParam;
        drawView.setFontSize(Integer.parseInt(s.substring(s.indexOf("PenSize=") + 8, s.indexOf("|PenColor"))));
        drawView.setDrawColor(Color.parseColor("#" + s.substring(s.indexOf("PenColor=") + 9, s.indexOf("|EraseSize"))));
    }


    @Override
    public NotifyWhiteboardOperator getNotifyWhiteboard(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String actionType = jsonObject.getString("ActionType");
            if (actionType.equals(Notify_WhiteboardOperator)) {
                return JsonUtil.parseJson(msg, NotifyWhiteboardOperator.class);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //获得请求下课msg
    public ResponseFinishClassData getNotify_FinishClass(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String actionType = jsonObject.getString("ActionType");
            if (actionType.equals("Response_FinishClass")) {
                return JsonUtil.parseJson(msg, ResponseFinishClassData.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public void drawPoint(DrawView view, NotifyWhiteboardOperator json) {
        List<String[]> datas = new ArrayList<>();
        String type = json.NotifyParam.MethodType;
        if (type.equals(MethodType.POINT) || type.equals(MethodType.LINE) || type.equals(MethodType.EraserPoint)) {
            String[] spit = json.NotifyParam.MethodParam.split("[|]");
            for (String str : spit) {
                String[] data = new String[2];
                String[] xyAxle = str.split(",");
                data[0] = xyAxle[0];
                data[1] = xyAxle[1];
                datas.add(data);
            }
        }
        if (datas.size() > 0) {
            String[] xyAxle = datas.get(0);
            view.eventActionDown(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
            if (datas.size() == 2) {
                String[] xyAxle2 = datas.get(1);
                view.eventActionMove(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
                view.eventActionUp(Float.parseFloat(xyAxle2[0]), Float.parseFloat(xyAxle2[1]));
            } else {
                for (int i = 1; i < datas.size() - 1; i++) {
                    view.eventActionMove(Float.parseFloat(datas.get(i)[0]), Float.parseFloat(datas.get(i)[1]));
                }
                view.eventActionUp(Float.parseFloat(datas.get(datas.size() - 1)[0]), Float.parseFloat(datas.get(datas.size() - 1)[1]));
            }
        }
    }

    @Override
    public void ScrollDrawView(MyScrollView view, NotifyWhiteboardOperator bean) {
        //TODO
//        String s = "255,0,520,520";  表示为视区矩形范围,X,Y,WIDTH,HEIGHT
        String s = bean.NotifyParam.MethodParam;
        String[] spit = s.split(",");
//        if(view.getScrollY()){

//        }


//        view.scrollTo(0,yAxis);
    }

    @Override
    public void drawEraser(DrawView view, NotifyWhiteboardOperator json) {
        drawPoint(view, json);
    }

    @Override
    public void drawOval(DrawView view, NotifyWhiteboardOperator json) {
        String s = json.NotifyParam.MethodParam;
        String[] xyAxle = s.split(",");
        view.eventActionDown(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
        view.eventActionMove(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
        int x = Integer.parseInt(xyAxle[0]) + Integer.parseInt(xyAxle[2]);
        int y = Integer.parseInt(xyAxle[1]) + Integer.parseInt(xyAxle[3]);
        view.eventActionUp(x, y);
    }

    @Override
    public void drawRectangle(DrawView view, NotifyWhiteboardOperator json) {
        drawOval(view, json);
    }

    @Override
    public int getActionType(NotifyWhiteboardOperator bean) {
        String type = bean.NotifyParam.MethodType;
        if (type.equals(MethodType.POINT)) {
            return DRAW;
        }
        if (type.equals(MethodType.LINE)) {
            return Line;
        }
        if (type.equals(MethodType.PaintSet)) {
            return SET;
        }
        if (type.equals(MethodType.ViewRect)) {
            return SCROLL;
        }
        if (type.equals(MethodType.EraserPoint)) {
            return Eraser;
        }
        if (type.equals(MethodType.PaintEllipse)) {
            return Oval;
        }
        if (type.equals(MethodType.PaintRect)) {
            return Rect;
        }
        if (type.equals(MethodType.Destory)) {
            return Destory;
        }
        if (type.equals(MethodType.Create)) {
            return Create;
        }
        return 0;
    }

    //课时消耗
    @Override
    public void uploadClassConsumption(String courseUuid) {
        Flowable<onlyHttpResponse<ClassConsumption>> flowable = mRetrofitHelper.fetchUploadClassConsumption(courseUuid);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showClassConsumption(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
