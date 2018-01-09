package com.onlyhiedu.pro.UI.Home.presenter;

import com.onlyhiedu.pro.App.Constants;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.CourseList;
import com.onlyhiedu.pro.Model.bean.RoomInfo;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.Home.presenter.contract.SmallCourseContract;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/3/21.
 */

public class SmallCoursePresenter extends RxPresenter<SmallCourseContract.View> implements SmallCourseContract.Presenter {

    private int mNoStartPage = 1;
    private int mEndPage;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public SmallCoursePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getCourseList(boolean isRefresh) {
        if (isRefresh) {
            mNoStartPage = 1;
        } else {
            mNoStartPage++;
        }

        Flowable<onlyHttpResponse<CourseList>> flowable = mRetrofitHelper.fetchGetNoEndCourseList(mNoStartPage);

        ResourceSubscriber<onlyHttpResponse<CourseList>> observer = new ResourceSubscriber<onlyHttpResponse<CourseList>>() {

            @Override
            public void onNext(onlyHttpResponse<CourseList> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError() ) {
                        getView().showCourseListSuccess(data.getData().list,isRefresh);
                    } else {
                        getView().showCourseListFailure();
                        getView().showError(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                if (getView() != null) getView().showNetWorkError();
            }

            @Override
            public void onComplete() {

            }

        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getEndCourseList(boolean isRefresh) {
        if (isRefresh) {
            mEndPage = 1;
        } else {
            mEndPage++;
        }

        Flowable<onlyHttpResponse<CourseList>> flowable = mRetrofitHelper.fetchGetCourseRecordList(mEndPage);

        ResourceSubscriber<onlyHttpResponse<CourseList>> observer = new ResourceSubscriber<onlyHttpResponse<CourseList>>() {

            @Override
            public void onNext(onlyHttpResponse<CourseList> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showCourseListSuccess(data.getData().list,isRefresh);
                    } else {
                        getView().showError(data.getMessage());
                        getView().showCourseListFailure();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                if (getView() != null) getView().showNetWorkError();
            }

            @Override
            public void onComplete() {

            }

        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getRoomInfoList(String uuid) {
        Flowable<onlyHttpResponse<RoomInfo>> flowable = mRetrofitHelper.fetchGetRoomInfoList(uuid);


        ResourceSubscriber<onlyHttpResponse<RoomInfo>> observer = new ResourceSubscriber<onlyHttpResponse<RoomInfo>>() {

            @Override
            public void onNext(onlyHttpResponse<RoomInfo> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        //返回的数据
                        RoomInfo bean = data.getData();
                        getView().showRoomInfoSucess(bean);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                getView().showError(Constants.NET_ERROR);
            }

            @Override
            public void onComplete() {

            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
