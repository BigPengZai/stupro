package io.agore.openvcall.ui;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.onlyhiedu.mobile.Utils.DateUtil;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Widget.MyScrollView;
import com.onlyhiedu.mobile.Widget.draw.DrawView;

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
    public static final int PaintText = 10;
    public static final int EraserRect = 11;
    public static final int ChangeDoc = 12; //改变白板关联的课件
    public static final int ClearScreen = 13;


    private RetrofitHelper mRetrofitHelper;

    private float mRate;   //缩放比例
    private int mImageWidth;//半屏的时候白板宽度

    public void setRate(float rate) {
        mRate = rate;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public float getRate() {
        return mRate;
    }

    @Inject
    public ChatPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getCourseWareImageList(String wareId, int page) {

        Flowable<onlyHttpResponse<List<CourseWareImageList>>> flowable = mRetrofitHelper.fetchGetCoursewareImageList(wareId);

        MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>> subscriber = new MyResourceSubscriber<onlyHttpResponse<List<CourseWareImageList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<CourseWareImageList>> data) {
                if (getView() != null) {
                    if (!data.isHasError()) getView().showCourseWareImageList(data.getData(), page);
                    else getView().showError(data.getMessage());
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, subscriber));
    }

    @Override
    public void setDrawableStyle(DrawView drawView, ResponseWhiteboardList data, ImageView courseWareImage) {

        ResponseWhiteboardList.ResponseParamBean.WhiteboardListBean bean = data.ResponseParam.WhiteboardList.get(0);

        float rate = (float) mImageWidth / (float) bean.WhiteboardWidth;

        setRate(rate);

        if (!TextUtils.isEmpty(bean.WhiteboardDocID) && !TextUtils.isEmpty(bean.WhiteboardDocID)) {
            getCourseWareImageList(bean.WhiteboardDocID, Integer.parseInt(bean.WhiteboardDocPageID));
        } else {
            //设置比例转换白板的宽度和高度
            int imageHeight = (int) ((float) bean.WhiteboardHeight * rate);
            courseWareImage.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
            drawView.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
            drawView.setCanvas(mImageWidth, imageHeight);
        }

        //设置比例转换后的画笔大小
        drawView.setDrawWidth((float) (bean.WhiteboardPenSize) * rate);

        //设置比例转换后的橡皮檫尺寸
        drawView.setEraserSize(((float) (bean.WhiteboardEraseSize) * rate));

        //设置画笔颜色
        drawView.setDrawColor(Color.parseColor("#" + bean.WhiteboardPenColor));

        //设置比例转化后的字体大小
        String fontType = bean.WhiteboardFontType;
        String[] split = fontType.split(",");
        String str = split[split.length - 1];
        float fintSize = parseFloat(str.substring(1, str.length()));
        drawView.setFontSize(fintSize * rate);

    }

    @Override
    public void setDrawableStyle(DrawView drawView, NotifyWhiteboardOperator data) {
        String s = data.NotifyParam.MethodParam;
        drawView.setDrawWidth(Integer.parseInt(s.substring(s.indexOf("PenSize=") + 8, s.indexOf("|PenColor"))));
        drawView.setDrawColor(Color.parseColor("#" + s.substring(s.indexOf("PenColor=") + 9, s.indexOf("|EraserSize"))));
    }

    @Override
    public void setBoardCreate(ImageView courseWareImage, DrawView drawView, NotifyWhiteboardOperator data) {

        //设置比例转换后的白板宽高
        String[] split = data.NotifyParam.MethodParam.split("[|]");
        float pcBoardWidth = parseFloat(split[2].split("=")[1]);
        float pcBoardHeight = parseFloat(split[3].split("=")[1]);

        float rate = (float) mImageWidth / pcBoardWidth;
        setRate(rate);
        int imageHeight = (int) (pcBoardHeight * rate);


        //设置比例转换白板的宽度和高度
        courseWareImage.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
        drawView.setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, imageHeight));
        drawView.setCanvas(mImageWidth, imageHeight);

        //设置比例转换后的画笔大小
        drawView.setDrawWidth((Float.parseFloat(split[7].split("=")[1]) * rate));

        //设置画笔颜色
        drawView.setDrawColor(Color.parseColor("#" + split[8].split("=")[1]));

        //设置比例转换后的橡皮檫尺寸
        drawView.setEraserSize(((Float.parseFloat(split[9].split("=")[1]) * rate)));

        //设置比例转化后的字体大小
        String FontTypeStr = split[10].split("=")[1];
        String fontSize = FontTypeStr.substring(FontTypeStr.lastIndexOf("#") + 1, FontTypeStr.length());
        drawView.setFontSize(((Float.parseFloat(fontSize) * rate)));


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
            view.eventActionDown(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);
            if (datas.size() == 2) {
                String[] xyAxle2 = datas.get(1);
                view.eventActionMove(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);
                view.eventActionUp(parseFloat(xyAxle2[0]) * mRate, parseFloat(xyAxle2[1]) * mRate);
            } else {
                for (int i = 1; i < datas.size() - 1; i++) {
                    view.eventActionMove(parseFloat(datas.get(i)[0]) * mRate, parseFloat(datas.get(i)[1]) * mRate);
                }
                view.eventActionUp(parseFloat(datas.get(datas.size() - 1)[0]) * mRate, parseFloat(datas.get(datas.size() - 1)[1]) * mRate);
            }
        }
    }

    @Override
    public void ScrollDrawView(Activity activity, MyScrollView view, NotifyWhiteboardOperator bean) {
//        String s = "255,0,520,520";  表示为视区矩形范围,X,Y,WIDTH,HEIGHT
//        String s = bean.NotifyParam.MethodParam;
//        String[] spit = s.split(",");
//
//        int height = getScreenHeight(activity) - getToolbarHeight(activity);
//
//        int viewHeight = view.getScrollY();
//
//        int y = (int) (Float.parseFloat(spit[1]) * mRate);
//        if (y > height) {
//            view.scrollTo(0, y);
//        }

//        int height = getScreenHeight(activity) - getToolbarHeight(activity);
//
//        float visibilityRegion = (float) (height) / mRate;
    }

    @Override
    public void drawEraser(DrawView view, NotifyWhiteboardOperator json) {
        drawPoint(view, json);
    }

    @Override
    public void drawEraserRect(DrawView view, NotifyWhiteboardOperator json) {
        drawOval(view, json);
    }

    @Override
    public void drawOval(DrawView view, NotifyWhiteboardOperator json) {
        String s = json.NotifyParam.MethodParam;
        String[] xyAxle = s.split(",");
        view.eventActionDown(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);
        view.eventActionMove(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);

        float x = parseFloat(xyAxle[0]) * mRate + parseFloat(xyAxle[2]) * mRate;
        float y = parseFloat(xyAxle[1]) * mRate + parseFloat(xyAxle[3]) * mRate;
        view.eventActionUp(x, y);
    }

    @Override
    public void drawRectangle(DrawView view, NotifyWhiteboardOperator json) {
        drawOval(view, json);
    }

    @Override
    public void drawText(DrawView view, NotifyWhiteboardOperator json) {
        String s = json.NotifyParam.MethodParam;
        String spit[] = s.split("[|]");
        String[] xyAxle = spit[0].split(",");
        view.eventActionDown(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);
        view.eventActionMove(parseFloat(xyAxle[0]) * mRate, parseFloat(xyAxle[1]) * mRate);
        float x = parseFloat(xyAxle[0]) * mRate + parseFloat(xyAxle[2]) * mRate;
        float y = parseFloat(xyAxle[1]) * mRate + parseFloat(xyAxle[3]) * mRate;
        view.eventActionUp(x, y);
        view.refreshLastText(spit[1]);
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
        if (type.equals(MethodType.PaintText)) {
            return PaintText;
        }
        if (type.equals(MethodType.EraserRect)) {
            return EraserRect;
        }
        if (type.equals(MethodType.ChangeDoc)) {
            return ChangeDoc;
        }
        if (type.equals(MethodType.ClearScreen)) {
            return ClearScreen;
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


//    String peer = mRoomInfo.getChannelTeacherId() + "";
//    //发送点对点 消息
//    ResponseFinishClassData finish = new ResponseFinishClassData();
//    ResponseFinishClassData.ResponseParam responParamBean = new ResponseFinishClassData.ResponseParam();
//    finish.AccountID = mRoomInfo.getChannelStudentId() + "";
//    finish.ActionType = "Response_FinishClass";
//    finish.Keyword = "HKT";
//    finish.ChannelID = mRoomInfo.getCommChannelId();
//    finish.ResponseParam = responParamBean;
//    responParamBean.Confirm = "NO";
//    responParamBean.FinishTime = SystemClock.currentThreadTimeMillis() + "";
//    String json = JsonUtil.toJson(finish);
//    m_agoraAPI.messageInstantSend(peer, 0, json, "stu_no");

    public String responseFinishClass(String  confirm,int stuId,String channelId){
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
        return  JsonUtil.toJson(finish);
    }




}
