package io.agore.openvcall.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;

import java.lang.reflect.Type;

import javax.inject.Inject;

import cn.robotpen.model.entity.note.TrailsEntity;
import cn.robotpen.model.symbol.DeviceType;
import cn.robotpen.views.widget.WhiteBoardView;
import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ChatPresenter2 extends RxPresenter<ChatContract2.View> implements ChatContract2.Presenter {

    private RetrofitHelper mRetrofitHelper;

    private Gson mGson;

    @Inject
    public ChatPresenter2(RetrofitHelper mRetrofitHelper) {
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


    public TrailsEntity getJson(String s) {
        if (mGson == null) {
            mGson = new Gson();
        }
        Type listType = new TypeToken<TrailsEntity>() {
        }.getType();
        return mGson.fromJson(s, listType);
    }

    public void drawDevicePoint(String msg, WhiteBoardView view) {
        String[] split = msg.split("[|]");
        for (String str : split) {
            String[] data = str.split("_");
            DeviceType type = DeviceType.toDeviceType(Integer.valueOf(data[1]));
            byte state = 0;
            switch (data[5]) {
                case "A":
                    state = 0;
                    break;
                case "B":
                    state = 17;
                    break;
                case "C":
                    state = 16;
                    break;
            }
            view.drawDevicePoint(type, Integer.valueOf(data[2]), Integer.valueOf(data[3]), Integer.valueOf(data[4]), state);
//            view.drawLine();
        }
    }


}