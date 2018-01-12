package com.onlyhiedu.pro.Listener;

import android.content.Context;
import android.view.MenuItem;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.onlyhiedu.pro.IM.SettingsActivity;
import com.onlyhiedu.pro.IM.contact.activity.AddFriendActivity;
import com.onlyhiedu.pro.IM.team.activity.AdvancedTeamSearchActivity;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;

/**
 * Created by Administrator on 2018/1/12.
 */

public class HomeMenuItemClickListener implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

    private Context mContext;

    public HomeMenuItemClickListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact:
                AddFriendActivity.start(mContext);
                break;
            case R.id.create_discussion:
                ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(mContext, option, MainActivity.REQUEST_CODE_NORMAL);
                break;
            case R.id.create_group:
                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(mContext, advancedOption, MainActivity.REQUEST_CODE_ADVANCED);
                break;
            case R.id.search_group:
                AdvancedTeamSearchActivity.start(mContext);
                break;
            case R.id.uikit_setting:
                SettingsActivity.start(mContext);
                break;
        }

        return false;
    }
}
