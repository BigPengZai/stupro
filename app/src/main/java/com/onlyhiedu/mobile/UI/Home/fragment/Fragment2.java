package com.onlyhiedu.mobile.UI.Home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onlyhiedu.mobile.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by xuwc on 2017/2/18.
 */

public class Fragment2 extends SupportFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycle_refresh, null);
    }
}
