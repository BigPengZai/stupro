package com.onlyhiedu.mobile.UI.Emc.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.EaseConversationListContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class EaseConversationListPresenter extends RxPresenter<EaseConversationListContract.View> implements EaseConversationListContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public EaseConversationListPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getIMUserFriendList() {
        Flowable<onlyHttpResponse<IMAllUserInfo>> flowable = mRetrofitHelper.fetchGetIMUserFriendList();

        MyResourceSubscriber<onlyHttpResponse<IMAllUserInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<IMAllUserInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<IMAllUserInfo> data) {

                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().getIMUserFriendListSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
