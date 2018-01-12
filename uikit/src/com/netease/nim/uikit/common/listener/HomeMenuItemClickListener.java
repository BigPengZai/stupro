package com.netease.nim.uikit.common.listener;

import android.content.Context;
import android.view.MenuItem;

import com.netease.nim.uikit.R;

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

        }
        return false;
    }
}
