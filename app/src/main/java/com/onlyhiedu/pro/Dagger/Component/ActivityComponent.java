package com.onlyhiedu.pro.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.pro.Dagger.ActivityScope;
import com.onlyhiedu.pro.Dagger.Modul.ActivityModule;
import com.onlyhiedu.pro.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.pro.UI.Course.activity.CoursePayActivity;
import com.onlyhiedu.pro.UI.Course.activity.EvaluateActivity;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;
import com.onlyhiedu.pro.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.pro.UI.Setting.activity.AboutActivity;
import com.onlyhiedu.pro.UI.Setting.activity.DeviceTestActivity;
import com.onlyhiedu.pro.UI.Setting.activity.FeedBackActivity;
import com.onlyhiedu.pro.UI.Setting.activity.ModifyPwActivity;
import com.onlyhiedu.pro.UI.User.activity.BindActivity;
import com.onlyhiedu.pro.UI.User.activity.FindPwdActivity;
import com.onlyhiedu.pro.UI.User.activity.LoginActivity;
import com.onlyhiedu.pro.UI.User.activity.OpenIDActivity;
import com.onlyhiedu.pro.UI.User.activity.RegActivity;
import com.onlyhiedu.pro.UI.User.activity.SmsLoginActivity;

import dagger.Component;
import io.agore.openvcall.ui.ChatActivity;
import io.agore.openvcall.ui.ChatActivity2;

/**
 * Created by xuwc on 2016/11/24.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(SmsLoginActivity activity);

    void inject(FindPwdActivity activity);

    void inject(LoginActivity activity);

    void inject(ModifyPwActivity activity);

    void inject(AboutActivity aboutActivity);

    void inject(ChatActivity activity);

    void inject(FeedBackActivity feedBackActivity);

    void inject(MyInfoActivity myInfoActivity);

    void inject(ConsumeActivity consumeActivity);

    void inject(MainActivity mainActivity);

    void inject(BindActivity bindActivity);

    void inject(OpenIDActivity openIDActivity);

    void inject(RegActivity activity);

    void inject(CoursePayActivity coursePayActivity);

    void inject(EvaluateActivity activity);

    void inject(ChatActivity2 chatActivity2);

    void inject(DeviceTestActivity activity);
}
