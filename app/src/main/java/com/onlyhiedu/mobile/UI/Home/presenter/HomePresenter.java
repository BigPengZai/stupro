package com.onlyhiedu.mobile.UI.Home.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;
import com.onlyhiedu.mobile.Model.bean.TypeListInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.HomeContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public HomePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getBannerData() {
        Flowable<onlyHttpResponse<HomeBannerBean>> flowable = mRetrofitHelper.fetchGetListBanner();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<HomeBannerBean>>() {

            @Override
            public void onNextData(onlyHttpResponse<HomeBannerBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showBannerData(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getTeacherData() {
        Flowable<onlyHttpResponse<HomeTeacher>> flowable = mRetrofitHelper.fetchGetListTeacher();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<HomeTeacher>>() {


            @Override
            public void onNextData(onlyHttpResponse<HomeTeacher> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showTeacherData(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getArticle() {
        Flowable<onlyHttpResponse<HomeBannerBean>> flowable = mRetrofitHelper.fetchGetListArticle();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<HomeBannerBean>>() {

            @Override
            public void onNextData(onlyHttpResponse<HomeBannerBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showArticle(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void getActivityTypeList() {
        Flowable<onlyHttpResponse<List<TypeListInfo>>> flowable = mRetrofitHelper.fetchGetTypeList();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<List<TypeListInfo>>>() {

            @Override
            public void onNextData(onlyHttpResponse<List<TypeListInfo>> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().showTypeListSucess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }


}
