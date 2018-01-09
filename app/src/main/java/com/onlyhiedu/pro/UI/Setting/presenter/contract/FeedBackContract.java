package com.onlyhiedu.pro.UI.Setting.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;

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
