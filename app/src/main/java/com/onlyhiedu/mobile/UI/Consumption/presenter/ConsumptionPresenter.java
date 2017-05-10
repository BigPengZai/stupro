package com.onlyhiedu.mobile.UI.Consumption.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Consumption.presenter.contract.ConsumptionContract;

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
