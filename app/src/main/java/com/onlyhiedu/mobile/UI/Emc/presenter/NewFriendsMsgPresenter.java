package com.onlyhiedu.mobile.UI.Emc.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.NewFriendsMsgContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/6.
 */

public class NewFriendsMsgPresenter extends RxPresenter<NewFriendsMsgContract.View> implements NewFriendsMsgContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public NewFriendsMsgPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void addFriends(String phone) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchaddFriends(phone);

        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
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
