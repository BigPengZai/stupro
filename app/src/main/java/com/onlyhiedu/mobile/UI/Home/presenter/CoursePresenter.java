package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.CourseList;
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

    private int mCurrentPage = 1;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public CoursePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getCourseList(boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }

        Flowable<onlyHttpResponse<CourseList>> flowable = mRetrofitHelper.fetchGetNoStartCourseList(mCurrentPage);

        ResourceSubscriber<onlyHttpResponse<CourseList>> observer = new ResourceSubscriber<onlyHttpResponse<CourseList>>() {

            @Override
            public void onNext(onlyHttpResponse<CourseList> data) {
                if (getView() != null) {
                    if (!data.isHasError()) {
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


}
