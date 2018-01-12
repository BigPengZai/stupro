package com.onlyhiedu.pro.Listener;

import android.content.Context;
import android.view.MenuItem;

import com.netease.nim.uikit.R;
import com.onlyhiedu.pro.IM.SettingsActivity;
import com.onlyhiedu.pro.IM.contact.activity.AddFriendActivity;

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
        if (item.getItemId() == R.id.add_contact) {
            AddFriendActivity.start(mContext);
        }
        if (item.getItemId()==R.id.uikit_setting) {
            SettingsActivity.start(mContext);
        }
        return false;
    }
}
