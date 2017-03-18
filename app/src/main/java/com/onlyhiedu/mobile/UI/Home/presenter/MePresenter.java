package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.MeContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class MePresenter extends RxPresenter<MeContract.View> implements MeContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public MePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getStudentInfo() {

        Flowable<onlyHttpResponse<StudentInfo>> flowable = mRetrofitHelper.fetchStudentInfo();

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<StudentInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<StudentInfo> data) {
                if (getView() != null) {
                    if (!data.isHasError()) {
                        getView().showStudentInfo(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void updateSex(int sex) {

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateSex(sex);

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {

                    getView().showError(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


    @Override
    public void updateGrade(String grade) {

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateGrade(grade);

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {

                    getView().showError(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void updateExamArea(String examArea) {

        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchUpdateExamArea(examArea);

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null) {

                    getView().showError(data.getMessage());
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

}
