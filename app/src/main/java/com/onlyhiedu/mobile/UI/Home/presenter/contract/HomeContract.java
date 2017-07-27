package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;
import com.onlyhiedu.mobile.Model.bean.TypeListInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface HomeContract {

    interface View extends BaseView {
        void showBannerData(HomeBannerBean data);
        void showTeacherData(HomeTeacher data);
        void showArticle(HomeBannerBean data);

        void showTypeListSucess(List<TypeListInfo> data);
    }

    interface Presenter extends BasePresenter<HomeContract.View> {
        void getBannerData();
        void getTeacherData();
        void getArticle();
        //活动列表
        void getActivityTypeList();
    }
}
