package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.CourseContract;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CoursePresenter extends RxPresenter<CourseContract.View> implements CourseContract.Presenter {

    private int mNoStartPage = 1;
    private int mEndPage;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public CoursePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getCourseList(boolean isRefresh) {
        if (isRefresh) {
            mNoStartPage = 1;
        } else {
            mNoStartPage++;
        }

        Flowable<onlyHttpResponse<CourseList>> flowable = mRetrofitHelper.fetchGetNoStartCourseList(mNoStartPage);

        ResourceSubscriber<onlyHttpResponse<CourseList>> observer = new ResourceSubscriber<onlyHttpResponse<CourseList>>() {

            @Override
            public void onNext(onlyHttpResponse<CourseList> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError() && data.getData() != null && data.getData().list != null) {
                        getView().showCourseListSuccess(data.getData().list);
                    } else {
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
    public void getEndCourseList(boolean isRefresh) {
        if (isRefresh) {
            mEndPage = 1;
        } else {
            mEndPage++;
        }

        Flowable<onlyHttpResponse<CourseList>> flowable = mRetrofitHelper.fetchGetEndCourseList(mEndPage);

        ResourceSubscriber<onlyHttpResponse<CourseList>> observer = new ResourceSubscriber<onlyHttpResponse<CourseList>>() {

            @Override
            public void onNext(onlyHttpResponse<CourseList> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showCourseListSuccess(data.getData().list);
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

        MyResourceSubscriber<onlyHttpResponse<RoomInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<RoomInfo>>() {

            @Override
            public void onNextData(onlyHttpResponse<RoomInfo> data) {
                if (getView() != null) {
                    if (!data.isHasError()) {
                        //返回的数据
                        RoomInfo bean = data.getData();
                        getView().showRoomInfoSucess(bean);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
