package io.agore.openvcall.ui;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;

/**
 * Created by Administrator on 2017/3/25.
 */

public interface ChatContract2 {


    interface View extends BaseView {
        //流统计 Flow statistics
        void showFlowStatistics();

    }

    interface Presenter extends BasePresenter<ChatContract2.View> {
        //上传流统计
        void uploadStatistics(String classTime, String courseUuid);

    }
}
