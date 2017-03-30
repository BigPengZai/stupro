package com.onlyhiedu.mobile.UI.Setting.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;

/**
 * Created by pengpeng on 2017/3/29.
 */

public interface FeedBackContract {
    interface View extends BaseView{
        void showFeedBackSuccess(String msg);
    }
    interface Presenter extends BasePresenter<FeedBackContract.View>{
        void sendFeedBack(String content);
    }
}
