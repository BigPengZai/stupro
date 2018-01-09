package com.onlyhiedu.pro.UI.Course.persenter;

import com.onlyhiedu.pro.App.Constants;
import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.StarContentList;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.Course.persenter.contract.EvaluateContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/9/12.
 */

public class EvaluatePresenter extends RxPresenter<EvaluateContract.View> implements EvaluateContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public EvaluatePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getStarContextList(int num) {
        Flowable<onlyHttpResponse<List<StarContentList>>> flowable = mRetrofitHelper.fetchGetStarContentList(num);

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<List<StarContentList>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<StarContentList>> data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().showStarContextList(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void SaveAppraise(int num, String classAppraiseStarUuids, String remark, String courseUuid) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchSaveAppraise(num, classAppraiseStarUuids, remark, courseUuid);

        ResourceSubscriber observer = new ResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNext(onlyHttpResponse data) {
                if (null != getView() && null != data) {
                    if (!data.isHasError()) {
                        getView().saveAppraiseSuccess(data.getMessage());
                    } else {
                        getView().saveAppraiseFailure(data.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                getView().saveAppraiseFailure(Constants.NET_ERROR);
            }

            @Override
            public void onComplete() {

            }


        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }
}
