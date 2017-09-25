package com.onlyhiedu.mobile.Dagger.Component;

import android.app.Activity;

import com.onlyhiedu.mobile.Dagger.ActivityScope;
import com.onlyhiedu.mobile.Dagger.Modul.ActivityModule;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.mobile.UI.Course.activity.CoursePayActivity;
import com.onlyhiedu.mobile.UI.Course.activity.EvaluateActivity;
import com.onlyhiedu.mobile.UI.Emc.AddContactActivity;
import com.onlyhiedu.mobile.UI.Emc.NewFriendsMsgActivity;
import com.onlyhiedu.mobile.UI.Emc.UserProfileActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.AboutActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.FeedBackActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.ModifyPwActivity;
import com.onlyhiedu.mobile.UI.User.activity.BindActivity;
import com.onlyhiedu.mobile.UI.User.activity.FindPwdActivity;
import com.onlyhiedu.mobile.UI.User.activity.LoginActivity;
import com.onlyhiedu.mobile.UI.User.activity.OpenIDActivity;
import com.onlyhiedu.mobile.UI.User.activity.RegActivity;
import com.onlyhiedu.mobile.UI.User.activity.SmsLoginActivity;

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

    void inject(NewFriendsMsgActivity newFriendsMsgActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(OpenIDActivity openIDActivity);

    void inject(RegActivity activity);

    void inject(AddContactActivity activity);

    void inject(CoursePayActivity coursePayActivity);

    void inject(EvaluateActivity activity);

    void inject(ChatActivity2 chatActivity2);
}
