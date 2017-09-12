package com.onlyhiedu.mobile.UI.Course.persenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.StarContentList;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface EvaluateContract {

    interface View extends BaseView {
        void showStarContextList(List<StarContentList> data);

        void saveAppraiseSuccess(String message);

        void saveAppraiseFailure(String message);
    }

    interface Presenter extends BasePresenter<EvaluateContract.View> {

        void getStarContextList(int num);

        void SaveAppraise(int num, String classAppraiseStarUuids, String remark, String courseUuid);

    }

}
