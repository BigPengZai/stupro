package io.agore.openvcall.ui;

import android.graphics.Color;
import android.text.TextUtils;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.board.MyBoardData;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.Widget.draw.DrawView;
import com.onlyhiedu.mobile.Widget.draw.DrawingMode;
import com.onlyhiedu.mobile.Widget.draw.DrawingTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

import static java.lang.Float.parseFloat;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    public static final String PEN = "03";
    public static final String Eraser = "18";
    public static final String Rectangle = "04";
    public static final String Oval = "05";
    public static final String Line = "22";

    private float mFullScreenRate;  //全屏缩放比例
    private float mHalfScreenRate;  //半屏缩放比例

    private boolean mFullScreen;  //全屏？

    public void setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }

    public float getScreenRate() {
        return mFullScreen ? mFullScreenRate : mHalfScreenRate;
    }

    public void setHalfScreenRate(float halfScreenRate) {
        mHalfScreenRate = halfScreenRate;
    }

    public void setFullScreenRate(float fullScreenRate) {
        mFullScreenRate = fullScreenRate;
    }

    @Inject
    public ChatPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    //流统计
    @Override
    public void uploadStatistics(String classTime, String courseUuid) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchStatics(classTime, courseUuid);
        MyResourceSubscriber<onlyHttpResponse> subscriber = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {
                    if (!data.isHasError()) getView().showFlowStatistics();
                    else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }


    @Override
    public void getCourseWareImageList(String wareId, int pageNum, boolean restart) {
        Flowable<onlyHttpResponse<List<CourseWareImageList>>> flowable = mRetrofitHelper.fetchGetCourseWareImageList(wareId);

        MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>> subscriber = new MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CourseWareImageList>> data) {
                if (getView() != null) {
                    if (!data.isHasError())
                        getView().showCourseWareImageList(data.getData(), pageNum, restart);
                    else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }


    private ArrayList<MyBoardData> drawData = new ArrayList<>();


    public void reDraw(DrawView view) {
        for (int t = 0; t < drawData.size(); t++) {
            MyBoardData myBoardData = drawData.get(t);
            if (myBoardData.type.equals(PEN)) {
                view.setDrawWidth(myBoardData.lineWidth);
                view.setDrawColor(myBoardData.color);
                setDrawingMode(view, PEN);
                drawLine(view, myBoardData, mFullScreen ? myBoardData.fullRate : myBoardData.halfRate);
            }
            if (myBoardData.type.equals(Eraser)) {
                setDrawingMode(view, Eraser);
                drawLine(view, myBoardData, mFullScreen ? myBoardData.fullRate : myBoardData.halfRate);
            }
            if (myBoardData.type.equals(Rectangle)) {
                setDrawingMode(view, Rectangle);
                drawRectangle(view, myBoardData.XYData, mFullScreen ? myBoardData.fullRate : myBoardData.halfRate);
            }
            if (myBoardData.type.equals(Oval)) {
                setDrawingMode(view, Oval);
                drawOval(view, myBoardData.XYData, mFullScreen ? myBoardData.fullRate : myBoardData.halfRate);
            }
            if (myBoardData.type.equals(Line)) {
                setDrawingMode(view, Line);
                drawLine(view, myBoardData, mFullScreen ? myBoardData.fullRate : myBoardData.halfRate);
            }
        }
    }

    public void add(String type, String data, int color, int lineWidth) {
        drawData.add(new MyBoardData(type, data, color, lineWidth, mFullScreenRate, mHalfScreenRate));
    }

    public void cleanDrawData(DrawView view) {
        drawData.clear();
        view.restartDrawing();
    }

    public void undo(DrawView view) {
        view.undo();
        if (drawData.size() > 0) {
            drawData.remove(drawData.size() - 1);
        }
    }

    public void initBoard(ChatActivity activity, DrawView view, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String boardWidth = jsonObject.getString("boardWidth");
            String boardHeight = jsonObject.getString("boardHeight");
            //设置白板宽高
            activity.setBoardViewLayoutParams(Integer.valueOf(boardWidth), Integer.valueOf(boardHeight));
            //设置课件

            try {
                String coursewareId = jsonObject.getString("coursewareId");
                if (!TextUtils.isEmpty(coursewareId)) {
                    getCourseWareImageList(coursewareId, Integer.valueOf(jsonObject.getString("pageNum")), false);
                }
            } catch (Exception e) {
            }

            //画线
            try {
                JSONArray drawData = jsonObject.getJSONArray("drawData");
                for (int i = 0; i < drawData.length(); i++) {
                    JSONObject bean = (JSONObject) drawData.opt(i);
                    switch (bean.getString("drawMode")) {
                        case PEN:
                            int lineWidth = bean.getInt("lineWidth");
                            view.setDrawWidth(lineWidth);
                            view.setDrawColor(Color.parseColor("#" + bean.getString("color")));
                            drawLine(view, bean.getString("points"));
                            add(ChatPresenter.PEN, bean.getString("points"), Color.parseColor("#" + bean.getString("color")), lineWidth);
                            break;
                    }
                }

            } catch (Exception e) {
            }

            String color = jsonObject.getString("color");
            if (!TextUtils.isEmpty(color)) {
                view.setDrawColor(Color.parseColor("#" + color));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void drawLine(DrawView view, String param) {

        List<String[]> datas = new ArrayList<>();
        String[] spit = param.split("[|]");
        for (String str : spit) {
            String[] data = new String[2];
            String[] xyAxle = str.split(",");
            data[0] = xyAxle[0];
            data[1] = xyAxle[1];
            datas.add(data);
        }
        if (datas.size() > 0) {
            String[] xyAxle = datas.get(0);
            view.eventActionDown(parseFloat(xyAxle[0]) * getScreenRate(), parseFloat(xyAxle[1]) * getScreenRate());
            if (datas.size() == 2) {
                String[] xyAxle2 = datas.get(1);
                view.eventActionMove(parseFloat(xyAxle[0]) * getScreenRate(), parseFloat(xyAxle[1]) * getScreenRate());
                view.eventActionUp(parseFloat(xyAxle2[0]) * getScreenRate(), parseFloat(xyAxle2[1]) * getScreenRate());
            } else {
                for (int i = 1; i < datas.size() - 1; i++) {
                    view.eventActionMove(parseFloat(datas.get(i)[0]) * getScreenRate(), parseFloat(datas.get(i)[1]) * getScreenRate());
                }
                view.eventActionUp(parseFloat(datas.get(datas.size() - 1)[0]) * getScreenRate(), parseFloat(datas.get(datas.size() - 1)[1]) * getScreenRate());
            }
        }
    }

    public void drawLine(DrawView view, MyBoardData param, float rate) {

        List<String[]> datas = new ArrayList<>();
        String[] spit = param.XYData.split("[|]");
        for (String str : spit) {
            String[] data = new String[2];
            String[] xyAxle = str.split(",");
            data[0] = xyAxle[0];
            data[1] = xyAxle[1];
            datas.add(data);
        }
        if (datas.size() > 0) {
            String[] xyAxle = datas.get(0);
            view.eventActionDown(parseFloat(xyAxle[0]) * rate, parseFloat(xyAxle[1]) * rate);
            if (datas.size() == 2) {
                String[] xyAxle2 = datas.get(1);
                view.eventActionMove(parseFloat(xyAxle[0]) * rate, parseFloat(xyAxle[1]) * rate);
                view.eventActionUp(parseFloat(xyAxle2[0]) * rate, parseFloat(xyAxle2[1]) * rate);
            } else {
                for (int i = 1; i < datas.size() - 1; i++) {
                    view.eventActionMove(parseFloat(datas.get(i)[0]) * rate, parseFloat(datas.get(i)[1]) * rate);
                }
                view.eventActionUp(parseFloat(datas.get(datas.size() - 1)[0]) * rate, parseFloat(datas.get(datas.size() - 1)[1]) * rate);
            }
        }
    }

    public void drawEraser(DrawView view, String param) {
        drawLine(view, param);
    }

    public void drawRectangle(DrawView view, String param, float rate) {
        drawOval(view, param, rate);
    }

    public void drawOval(DrawView view, String json, float rate) {
        String replace = json.replace("|", ",");
        String[] xyAxle = replace.split(",");

        view.eventActionDown(parseFloat(xyAxle[0]) * rate, parseFloat(xyAxle[1]) * rate);
        view.eventActionMove(parseFloat(xyAxle[0]) * rate, parseFloat(xyAxle[1]) * rate);

        float x = parseFloat(xyAxle[0]) * rate + parseFloat(xyAxle[2]) * rate;
        float y = parseFloat(xyAxle[1]) * rate + parseFloat(xyAxle[3]) * rate;
        view.eventActionUp(x, y);
    }


//    @Override
//    public void drawPoint(DrawView view, NotifyWhiteboardOperator json) {
//        List<String[]> datas = new ArrayList<>();
//        String type = json.NotifyParam.MethodType;
//        if (type.equals(MethodType.POINT) || type.equals(MethodType.LINE) || type.equals(MethodType.EraserPoint)) {
//            String[] spit = json.NotifyParam.MethodParam.split("[|]");
//            for (String str : spit) {
//                String[] data = new String[2];
//                String[] xyAxle = str.split(",");
//                data[0] = xyAxle[0];
//                data[1] = xyAxle[1];
//                datas.add(data);
//            }
//        }
//        if (datas.size() > 0) {
//            String[] xyAxle = datas.get(0);
//            view.eventActionDown(parseFloat(xyAxle[0]) * getRate(), parseFloat(xyAxle[1]) * getRate());
//            if (datas.size() == 2) {
//                String[] xyAxle2 = datas.get(1);
//                view.eventActionMove(parseFloat(xyAxle[0]) * getRate(), parseFloat(xyAxle[1]) * getRate());
//                view.eventActionUp(parseFloat(xyAxle2[0]) * getRate(), parseFloat(xyAxle2[1]) * getRate());
//            } else {
//                for (int i = 1; i < datas.size() - 1; i++) {
//                    view.eventActionMove(parseFloat(datas.get(i)[0]) * getRate(), parseFloat(datas.get(i)[1]) * getRate());
//                }
//                view.eventActionUp(parseFloat(datas.get(datas.size() - 1)[0]) * getRate(), parseFloat(datas.get(datas.size() - 1)[1]) * getRate());
//            }
//        }
//    }


//
//    @Override
//    public void drawEraserRect(DrawView view, NotifyWhiteboardOperator json) {
//        drawOval(view, json);
//    }
//

//

//
//    @Override
//    public void drawText(DrawView view, NotifyWhiteboardOperator json) {
//        String s = json.NotifyParam.MethodParam;
//        String spit[] = s.split("[|]");
//        String[] xyAxle = spit[0].split(",");
//        view.eventActionDown(parseFloat(xyAxle[0]) * mHalfScreenRate, parseFloat(xyAxle[1]) * mHalfScreenRate);
//        view.eventActionMove(parseFloat(xyAxle[0]) * mHalfScreenRate, parseFloat(xyAxle[1]) * mHalfScreenRate);
//        float x = parseFloat(xyAxle[0]) * mHalfScreenRate + parseFloat(xyAxle[2]) * mHalfScreenRate;
//        float y = parseFloat(xyAxle[1]) * mHalfScreenRate + parseFloat(xyAxle[3]) * mHalfScreenRate;
//        view.eventActionUp(x, y);
//        view.refreshLastText(spit[1]);
//    }


    public void setDrawingMode(DrawView view, String type) {
        switch (type) {
            case PEN:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[0]);
                break;
            case Eraser:
                view.setDrawingMode(DrawingMode.values()[2]);
                view.setDrawingTool(DrawingTool.values()[0]);
                break;
            case Rectangle:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[2]);
                break;
            case Oval:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[4]);
                break;
            case Line:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[1]);
                break;
        }

    }

}