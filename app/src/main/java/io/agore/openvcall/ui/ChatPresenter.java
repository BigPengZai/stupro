package io.agore.openvcall.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;

import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.CourseWareImageList;
import com.onlyhiedu.pro.Model.bean.board.MyBoardData;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.Utils.ImageLoader;
import com.onlyhiedu.pro.Widget.draw.DrawView;
import com.onlyhiedu.pro.Widget.draw.DrawingMode;
import com.onlyhiedu.pro.Widget.draw.DrawingTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

import static java.lang.Float.parseFloat;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    public int mBoardWidth;
    public int mBoardHeight;

    public int mCurrentBoardWidth;
    public int mCurrentBoardHeight;

    public static final String AfterJoin = "14";


    public static final String PEN = "01";
    public static final String Line = "02";
    public static final String Rectangle = "03";
    public static final String Oval = "04";
    public static final String Text = "05";
    public static final String Eraser = "07";

    private static float mFullScreenRate;  //全屏缩放比例
    private static float mHalfScreenRate;  //半屏缩放比例

    private static boolean mFullScreen;  //全屏？

    public void setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }

    public static float getScreenRate() {
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
                    if (!data.isHasError()) {
                        getView().showCourseWareImageList(data.getData(), pageNum, restart);
                    } else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }

    //结束课时
    @Override
    public void getUpdateEndTime(String courseUuid) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateEndTime(courseUuid);
        MyResourceSubscriber<onlyHttpResponse> subscriber = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {
                    if (!data.isHasError()) {
                        getView().showUpdateEndTime(data.getMessage());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }



    private LinkedList<MyBoardData> drawData = new LinkedList<>();


    public void reDraw(DrawView view) {
        for (int t = 0; t < drawData.size(); t++) {
            MyBoardData myBoardData = drawData.get(t);
            setDrawingMode(view, myBoardData.type);
            switch (myBoardData.type) {
                case PEN:
                    view.setDrawWidth(myBoardData.lineWidth * getScreenRate());
                    view.setDrawColor(myBoardData.color);
                    drawLine(view, myBoardData, getScreenRate());
                    break;
                case Eraser:
                    view.setEraserSize(myBoardData.lineWidth * getScreenRate());
                    drawLine(view, myBoardData, getScreenRate());
                    break;
                case Rectangle:
                    view.setDrawWidth(myBoardData.lineWidth * getScreenRate());
                    view.setDrawColor(myBoardData.color);
                    drawRectangle(view, myBoardData.XYData, getScreenRate());
                    break;
                case Oval:
                    view.setDrawWidth(myBoardData.lineWidth * getScreenRate());
                    view.setDrawColor(myBoardData.color);
                    drawOval(view, myBoardData.XYData, getScreenRate());
                    break;
                case Line:
                    view.setDrawWidth(myBoardData.lineWidth * getScreenRate());
                    view.setDrawColor(myBoardData.color);
                    drawLine(view, myBoardData, getScreenRate());
                    break;
                case Text:
                    view.setDrawColor(myBoardData.color);
                    view.setDrawWidth(1);
                    int fontSize = (int) (myBoardData.lineWidth * getScreenRate() + 0.5);
                    view.setFontSize(fontSize);
                    drawText(view, myBoardData.XYData, myBoardData.text);
                    break;
            }
        }
    }

    public void add(String type, String data, int color, float lineWidth, String text) {
        drawData.add(new MyBoardData(type, data, color, lineWidth, text/*, mFullScreenRate, mHalfScreenRate*/));
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

    public void changePage(ChatActivity activity, DrawView view, ImageView imageView, String data) {

        cleanDrawData(view);

        try {
            JSONObject jsonObject = new JSONObject(data);
            int pageNum = jsonObject.optInt("pageNum");
            ImageLoader.loadImage(activity,activity.mRequestManager, imageView, activity.mCourseWareImageLists.get(pageNum).imageUrl);
            drawPoints(jsonObject, view);
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
        view.eventActionUp(parseFloat(xyAxle[3]) * rate, parseFloat(xyAxle[4]) * rate);
    }


    public void drawText(DrawView view, String data, String text) {
        String replace = data.replace("|", ",");
        String[] xyAxle = replace.split(",");

        view.eventActionDown(parseFloat(xyAxle[0]) * getScreenRate(), parseFloat(xyAxle[1]) * getScreenRate());
        view.eventActionMove(parseFloat(xyAxle[0]) * getScreenRate(), parseFloat(xyAxle[1]) * getScreenRate());
        view.eventActionUp(parseFloat(xyAxle[3]) * getScreenRate(), parseFloat(xyAxle[4]) * getScreenRate());

        view.refreshLastText(text);
    }


    public void setDrawingMode(DrawView view, String type) {
        switch (type) {
            case PEN:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[0]);
                break;
            case Line:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[1]);
                break;
            case Rectangle:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[2]);
                break;
            case Oval:
                view.setDrawingMode(DrawingMode.values()[0]);
                view.setDrawingTool(DrawingTool.values()[4]);
                break;
            case Text:
                view.setDrawingMode(DrawingMode.values()[1]);
                break;
            case Eraser:
                view.setDrawingMode(DrawingMode.values()[2]);
                view.setDrawingTool(DrawingTool.values()[0]);
                break;
        }
    }

    public void AfterJoin(String methodparam, ChatActivity activity, DrawView view) {
        try {
            cleanDrawData(view);
            JSONObject jsonObject = new JSONObject(methodparam);
            mBoardWidth = jsonObject.getInt("boardWidth");
            mBoardHeight = jsonObject.getInt("boardHeight");
            mCurrentBoardWidth = mBoardWidth;
            mCurrentBoardHeight = mBoardHeight;
            activity.setBoardViewLayoutParams(mBoardWidth, mBoardHeight);


            boolean openDocFlag = jsonObject.optBoolean("openDocFlag");
            if (openDocFlag) {
                mBoardWidth = jsonObject.getInt("WhiteBoardWidth");
                mBoardHeight = jsonObject.getInt("WhiteBoardHeight");
            }

            drawPoints(jsonObject, view);

            String coursewareId = jsonObject.optString("coursewareId");
            if (!TextUtils.isEmpty(coursewareId)) {  //有课件
                int pageNum = jsonObject.optInt("pageNum");
                getCourseWareImageList(coursewareId, pageNum, false);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawPoints(JSONObject jsonObject, DrawView view) throws JSONException {

        String drawDataStr = jsonObject.optString("drawData");

        if (!TextUtils.isEmpty(drawDataStr)) {  //有数据
            JSONArray drawData = new JSONArray(drawDataStr);

            for (int i = 0; i < drawData.length(); i++) {
                String objStr = drawData.getString(i);
                JSONObject obj = new JSONObject(objStr);
                draw(obj, view);
            }
        }
    }


    public void draw2(String methodparam, DrawView view, ChatActivity activity) {
        try {
            JSONObject jsonObject = new JSONObject(methodparam);

            int boardWidth = jsonObject.getInt("boardWidth");
            int boardHeight = jsonObject.getInt("boardHeight");
            if (mCurrentBoardHeight != boardHeight && mCurrentBoardWidth != boardWidth) {
                mCurrentBoardHeight = boardHeight;
                mCurrentBoardWidth = boardWidth;
                activity.setBoardViewLayoutParams(boardWidth, boardHeight);
            }

            draw(jsonObject, view);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void draw(JSONObject jsonObject, DrawView view) throws JSONException {
        String points = jsonObject.optString("points");
        if (TextUtils.isEmpty(points)) {
            return;
        }
        String drawMode = jsonObject.getString("drawMode");

        String color = jsonObject.getString("color");
        String[] split = color.substring(1, color.length() - 1).split(",");
        view.setDrawColor(Color.argb(0, Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2])));

        String lineWidth = jsonObject.getString("lineWidth");
        view.setDrawWidth(Float.valueOf(lineWidth) * getScreenRate());

        setDrawingMode(view, drawMode);


        switch (drawMode) {
            case PEN: //画线
                drawLine(view, points);
                break;
            case Line: //直线
                drawLine(view, points);
                break;
            case Rectangle: //框
                drawRectangle(view, points, getScreenRate());
                break;
            case Oval: //画圆
                drawOval(view, points, getScreenRate());
                break;
            case Text:
                String text = jsonObject.getString("text");
                view.setDrawWidth(1);
                int size = jsonObject.getInt("size");
                int fontSize = (int) ((float) size * getScreenRate() + 0.5);
                view.setFontSize(fontSize);
                drawText(view, points, text);
                break;
            case Eraser:
                view.setEraserSize(Float.valueOf(lineWidth) * getScreenRate());
                drawEraser(view, points);
                break;

        }
        if (Text.equals(drawMode)) {
            add(drawMode, points, view.getDrawColor(), jsonObject.getInt("size"), jsonObject.getString("text"));
        } else {
            add(drawMode, points, view.getDrawColor(), Float.valueOf(lineWidth), null);
        }
    }

    public void initBoard(String methodparam, DrawView drawView, ChatActivity activity) {
        try {
            JSONObject object = new JSONObject(methodparam);
            mBoardWidth = object.getInt("boardWidth");
            mBoardHeight = object.getInt("boardHeight");
            mCurrentBoardWidth = mBoardWidth;
            mCurrentBoardHeight = mBoardHeight;
            activity.setBoardViewLayoutParams(mBoardWidth, mBoardHeight);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}