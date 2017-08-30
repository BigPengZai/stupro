package io.agore.openvcall.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.finishclass.ResponseFinishClassData;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.Utils.DateUtil;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Widget.draw.DrawView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

import static java.lang.Float.parseFloat;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter {

    public static final String PEN = "03";

    private RetrofitHelper mRetrofitHelper;


    private float mFullScreenRate;  //全屏缩放比例
    private float mHalfScreenRate;  //半屏缩放比例


    private boolean mFullScreen;  //全屏？

    public void setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }


    public float getScreenRate() {
        return mFullScreen ? mHalfScreenRate : mHalfScreenRate;
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
    public void getCourseWareImageList(String wareId, int pageNum,boolean restart) {
        Flowable<onlyHttpResponse<List<CourseWareImageList>>> flowable = mRetrofitHelper.fetchGetCourseWareImageList(wareId);

        MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>> subscriber = new MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CourseWareImageList>> data) {
                if (getView() != null) {
                    if (!data.isHasError())
                        getView().showCourseWareImageList(data.getData(), pageNum,restart);
                    else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }

//    @Override
//    public void setDrawableStyle(DrawView drawView, ResponseWhiteboardList data, ImageView courseWareImage) {
//
//        ResponseWhiteboardList.ResponseParamBean.WhiteboardListBean bean = data.ResponseParam.WhiteboardList.get(0);
//
//        float rate = (float) mImageWidth / (float) bean.WhiteboardWidth;
//
////        setHalfScreenRate(rate);
//
//        if (!TextUtils.isEmpty(bean.WhiteboardDocID) && !TextUtils.isEmpty(bean.WhiteboardDocID)) {
//            getCourseWareImageList(bean.WhiteboardDocID, Integer.parseInt(bean.WhiteboardDocPageID));
//        } else {
//            //设置比例转换白板的宽度和高度
//            int imageHeight = (int) ((float) bean.WhiteboardHeight * rate);
//            courseWareImage.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
//            courseWareImage.setImageResource(R.drawable.transparent);
//            drawView.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
//            drawView.setCanvas(mImageWidth, imageHeight);
//        }
//
//        //设置比例转换后的画笔大小
//        drawView.setDrawWidth((float) (bean.WhiteboardPenSize) * rate);
//
//        //设置比例转换后的橡皮檫尺寸
//        drawView.setEraserSize(((float) (bean.WhiteboardEraseSize) * rate));
//
//        //设置画笔颜色
//        drawView.setDrawColor(Color.parseColor("#" + bean.WhiteboardPenColor));
//
//        //设置比例转化后的字体大小
//        String fontType = bean.WhiteboardFontType;
//        String[] split = fontType.split(",");
//        String str = split[split.length - 1];
//        float fintSize = parseFloat(str.substring(1, str.length()));
//        drawView.setFontSize(fintSize * rate);
//
//    }

    //不带课件的白板（true），带课件的白板（false）
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


//    @Override
//    public void drawEraser(DrawView view, NotifyWhiteboardOperator json) {
//        drawPoint(view, json);
//    }
//
//    @Override
//    public void drawEraserRect(DrawView view, NotifyWhiteboardOperator json) {
//        drawOval(view, json);
//    }
//
//    @Override
//    public void drawOval(DrawView view, NotifyWhiteboardOperator json) {
//        String s = json.NotifyParam.MethodParam;
//        String[] xyAxle = s.split(",");
//        view.eventActionDown(parseFloat(xyAxle[0]) * mHalfScreenRate, parseFloat(xyAxle[1]) * mHalfScreenRate);
//        view.eventActionMove(parseFloat(xyAxle[0]) * mHalfScreenRate, parseFloat(xyAxle[1]) * mHalfScreenRate);
//
//        float x = parseFloat(xyAxle[0]) * mHalfScreenRate + parseFloat(xyAxle[2]) * mHalfScreenRate;
//        float y = parseFloat(xyAxle[1]) * mHalfScreenRate + parseFloat(xyAxle[3]) * mHalfScreenRate;
//        view.eventActionUp(x, y);
//    }
//
//    @Override
//    public void drawRectangle(DrawView view, NotifyWhiteboardOperator json) {
//        drawOval(view, json);
//    }
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


    public void startDrawViewFullAnimation(DrawView view, float rate) {
        ScaleAnimation scaleX = new ScaleAnimation(1.0f, rate, 1.0f, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleX.setDuration(0);

        ScaleAnimation scaleY = new ScaleAnimation(1.0f, 1.0f, 1.0f, rate,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0);
        scaleY.setDuration(0);

        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);
        set.addAnimation(scaleX);
        set.addAnimation(scaleY);
        view.startAnimation(set);

    }


    public void initBoard(ChatActivity activity, DrawView view, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String boardWidth = jsonObject.getString("boardWidth");
            String boardHeight = jsonObject.getString("boardHeight");
            //设置白板宽高
            activity.setBoardViewLayoutParams(Integer.valueOf(boardWidth), Integer.valueOf(boardHeight));
            //设置课件
            String coursewareId = jsonObject.getString("coursewareId");
            if (!TextUtils.isEmpty(coursewareId)) {
                getCourseWareImageList(coursewareId, Integer.valueOf(jsonObject.getString("pageNum")),false);
            }
            //画线
            try {
                JSONArray drawData = jsonObject.getJSONArray("drawData");
                for (int i = 0; i < drawData.length(); i++) {
                    JSONObject bean = (JSONObject) drawData.opt(i);
                    switch (bean.getString("drawMode")) {
                        case PEN:
                            view.setDrawColor(Color.parseColor("#" + bean.getString("color")));
                            drawLine(view, bean.getString("points"));
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


    public String responseFinishClass(String confirm, int stuId, String channelId) {
        //发送点对点 消息
        ResponseFinishClassData finish = new ResponseFinishClassData();
        ResponseFinishClassData.ResponseParamBean responseParamBean = new ResponseFinishClassData.ResponseParamBean();
        finish.AccountID = stuId + "";
        finish.ActionType = "Response_FinishClass";
        finish.Keyword = "HKT";
        finish.ChannelID = channelId;
        finish.ResponseParam = responseParamBean;
        responseParamBean.Confirm = confirm;
        responseParamBean.FinishTime = DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss);
        return JsonUtil.toJson(finish);
    }


}
