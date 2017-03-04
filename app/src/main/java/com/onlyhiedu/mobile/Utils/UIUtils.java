package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by xuwc on 2017/2/21.
 */

public class UIUtils {


    public static void startLoginActivity(Context context) {
        //TODO
    }


    public static void setRcDecorationAndLayoutManager(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.list_divider)
                .size(1)
                .build());
        recyclerView.setAdapter(adapter);
    }

    public static void setRecycleAdapter(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
