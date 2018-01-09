package com.onlyhiedu.pro.UI.Consumption.presenter;

import com.onlyhiedu.pro.Base.RxPresenter;
import com.onlyhiedu.pro.Model.bean.ConsumptionData;
import com.onlyhiedu.pro.Model.http.MyResourceSubscriber;
import com.onlyhiedu.pro.Model.http.RetrofitHelper;
import com.onlyhiedu.pro.Model.http.onlyHttpResponse;
import com.onlyhiedu.pro.UI.Consumption.presenter.contract.ConsumptionContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by pengpeng on 2017/4/15.
 */

public class ConsumptionPresenter extends RxPresenter<ConsumptionContract.View> implements ConsumptionContract.Presenter {
    private RetrofitHelper mRetrofitHelper;
    @Inject
    public ConsumptionPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }



    @Override
    public void getClassTimeInfo() {
        Flowable<onlyHttpResponse<List<ConsumptionData>>> flowable = mRetrofitHelper.fetchClassTimeInfo();
        MyResourceSubscriber<onlyHttpResponse<List<ConsumptionData>>> observer = new MyResourceSubscriber<onlyHttpResponse<List<ConsumptionData>>>() {
            @Override
            public void onNextData(onlyHttpResponse<List<ConsumptionData>> data) {
                if (getView() != null ) {
                    if (!data.isHasError()) {
                        getView().showSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
