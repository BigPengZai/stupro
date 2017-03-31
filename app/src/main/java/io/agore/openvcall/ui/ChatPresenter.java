package io.agore.openvcall.ui;

import android.graphics.Color;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.ClassConsumption;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.board.MethodType;
import com.onlyhiedu.mobile.Model.bean.board.NotifyWhiteboardOperator;
import com.onlyhiedu.mobile.Model.bean.board.ResponseWhiteboardList;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Widget.draw.DrawView;
import com.onlyhiedu.mobile.Widget.draw.DrawingMode;

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


    public static String Notify_WhiteboardOperator = "Notify_WhiteboardOperator";
    public static int DRAW = 1;
    public static int SET = 2;

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
//        String s =  "MethodParam" : "PenSize=5|PenColor=FFFF00FF|EraserSize=5"
        String color = "PenColor=";
        String Size = "EraserSize=";
        String s = data.NotifyParam.MethodParam;

        drawView.setFontSize(Integer.valueOf(s.substring(8, s.indexOf(color) - 1)));
        drawView.setDrawColor(Color.parseColor("#" + s.substring(s.indexOf(color) + color.length(), s.indexOf(Size) - 1)));
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

    @Override
    public void drawPoint(DrawView view, NotifyWhiteboardOperator json) {

        List<String[]> datas = new ArrayList<>();
        String type = json.NotifyParam.MethodType;
        if (type.equals(MethodType.POINT) || type.equals(MethodType.LINE)) {
            String[] spit = json.NotifyParam.MethodParam.split("[|]");
            for (String str : spit) {
                String[] data = new String[2];
                String[] xyAxle = str.split(",");
                data[0] = xyAxle[0];
                data[1] = xyAxle[1];
                datas.add(data);
            }
        }
        view.setDrawingMode(DrawingMode.values()[0]);
        String[] xyAxle = datas.get(0);
        view.eventActionDown(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
        if (datas.size() == 2) {
            String[] xyAxle2 = datas.get(1);
            view.eventActionUp(Float.parseFloat(xyAxle2[0]), Float.parseFloat(xyAxle2[1]));
        } else {
            for (int i = 1; i < datas.size() - 1; i++) {
                view.eventActionMove(Float.parseFloat(datas.get(i)[0]), Float.parseFloat(datas.get(i)[1]));
            }
            view.eventActionUp(Float.parseFloat(datas.get(datas.size() - 1)[0]), Float.parseFloat(datas.get(datas.size() - 1)[1]));
        }
    }

    @Override
    public int getActionType(NotifyWhiteboardOperator bean) {
        String type = bean.NotifyParam.MethodType;
        if (type.equals(MethodType.POINT) || type.equals(MethodType.LINE)) {
            return DRAW;
        }
        if (type.equals(MethodType.PaintSet)) {
            return SET;
        }
        return 0;
    }


    @Override
    public void uploadClassConsumption(String courseUuid, String endTime) {
        Flowable<onlyHttpResponse<ClassConsumption>> flowable = mRetrofitHelper.fetchUploadClassConsumption(courseUuid, endTime);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {
                    getView().showClassConsumption(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
