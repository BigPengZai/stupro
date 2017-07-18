package com.onlyhiedu.mobile.UI.Emc.presenter;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.NewFriendsMsgContract;

import java.util.List;

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
    public void getNewFriends(List<String> IMNames) {
        Flowable<onlyHttpResponse<IMAllUserInfo>> flowable = mRetrofitHelper.fetchGetIMUserList(IMNames);
        MyResourceSubscriber<onlyHttpResponse<IMAllUserInfo>> observer = new MyResourceSubscriber<onlyHttpResponse<IMAllUserInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<IMAllUserInfo> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().getNewFriendsSuccess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }




}
