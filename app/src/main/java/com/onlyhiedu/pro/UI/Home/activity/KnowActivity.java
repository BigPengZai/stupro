package com.onlyhiedu.pro.UI.Home.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.onlyhiedu.pro.Base.BaseRecyclerAdapter;
import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Home.adapter.WantKnowFragmentAdapter;
import com.onlyhiedu.pro.Utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/5/16.
 */

public class KnowActivity extends SimpleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayout() {
        return R.layout.activity_know;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("你可能想知道");
        WantKnowFragmentAdapter mAdapter = new WantKnowFragmentAdapter(this);
        ArrayList<String> items = new ArrayList<>();
        items.add("Q1: 嗨课堂App能做什么?");
        items.add("Q2: 可以通过哪些方式上课?");
        items.add("Q3: 有哪些课程类型可供选择?");
        items.add("Q4: 一对一课程有什么优势?");
        items.add("Q5: 如何取消已经预定的课程?");
        items.add("Q6: 嗨课堂公众号有哪些主要内容?");
        mAdapter.addAll(items);
        UIUtils.setRcDecorationAndLayoutManager(mContext, mRecyclerView, mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                Intent intent = new Intent(KnowActivity.this,KnowTitleActivity.class);
                intent.putExtra("position", position+1+"");
                startActivity(intent);
            }
        });
    }
}
