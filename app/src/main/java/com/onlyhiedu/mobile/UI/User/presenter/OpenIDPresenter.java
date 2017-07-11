package com.onlyhiedu.mobile.UI.User.presenter;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.User.presenter.contract.OpenIDContract;
import com.onlyhiedu.mobile.Utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/10.
 */

public class OpenIDPresenter  extends RxPresenter<OpenIDContract.View> implements OpenIDContract.Presenter{

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public OpenIDPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void isBindUser(SHARE_MEDIA share_media, String uid, String openid, String name, String gender, String iconurl, String city, String province, String country) {

        Flowable<onlyHttpResponse<AuthUserDataBean>> flowable = mRetrofitHelper.fetchIsBindUser(share_media, uid, openid, name, gender, iconurl, city, province, country, StringUtils.getDeviceId(App.getInstance()));

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<AuthUserDataBean>>() {
            @Override
            public void onNextData(onlyHttpResponse<AuthUserDataBean> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().isShowBingActivity(data.getData().token, data.getData().phone, data.getData().userName, share_media, uid);
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));

    }
}
