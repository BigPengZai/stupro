package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface HomeContract {

    interface View extends BaseView {
        void showBannerData(HomeBannerBean data);
        void showTeacherData(HomeTeacher data);
        void showArticle(HomeBannerBean data);
    }

    interface Presenter extends BasePresenter<HomeContract.View> {
        void getBannerData();
        void getTeacherData();
        void getArticle();
    }
}
