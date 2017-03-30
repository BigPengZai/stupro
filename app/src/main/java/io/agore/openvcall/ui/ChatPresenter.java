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
    public int getActionType(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String actionType = jsonObject.getString("ActionType");
            if (actionType.equals(Notify_WhiteboardOperator)) {

                return DRAW;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    @Override
    public List<String[]> parseDrawJson(String msg) {
        NotifyWhiteboardOperator json = JsonUtil.parseJson(msg, NotifyWhiteboardOperator.class);
        if (json != null) {

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
                return datas;
            }
        }
        return null;
    }

    @Override
    public void drawPoint(DrawView view, List<String[]> data) {
        view.setDrawingMode(DrawingMode.values()[0]);
        String[] xyAxle = data.get(0);
        view.eventActionDown(Float.parseFloat(xyAxle[0]), Float.parseFloat(xyAxle[1]));
        if (data.size() == 2) {
            String[] xyAxle2 = data.get(1);
            view.eventActionUp(Float.parseFloat(xyAxle2[0]), Float.parseFloat(xyAxle2[1]));
        } else {
            for (int i = 1; i < data.size() - 1; i++) {
                view.eventActionMove(Float.parseFloat(data.get(i)[0]), Float.parseFloat(data.get(i)[1]));
            }
            view.eventActionUp(Float.parseFloat(data.get(data.size() - 1)[0]), Float.parseFloat(data.get(data.size() - 1)[1]));
        }
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
        addSubscription(mRetrofitHelper.startObservable(flowable,observer));
    }


}
