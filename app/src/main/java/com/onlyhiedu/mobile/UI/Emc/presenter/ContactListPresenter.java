package com.onlyhiedu.mobile.UI.Emc.presenter;

import com.hyphenate.easeui.domain.EaseUser;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.ContactListContract;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/6.
 */

public class ContactListPresenter extends RxPresenter<ContactListContract.View> implements ContactListContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public ContactListPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void deleteFriends(EaseUser user) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchDeleteFriend(user.getUsername());

        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().deleteFriendSuccess(user);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
               /* if (getView() != null) {
                    getView().showError(data.getMessage());
                }*/
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}