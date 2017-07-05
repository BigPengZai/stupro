package com.onlyhiedu.mobile.UI.Emc;

import android.widget.FrameLayout;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/7/5.
 */

public class ContactListActivity extends SimpleActivity {
    @BindView(R.id.fr_contract)
    FrameLayout mFrameLayout;
    @Override
    protected int getLayout() {
        return R.layout.activity_contactlist;
    }

    @Override
    protected void initEventAndData() {
        ContactListFragment contactListFragment = new ContactListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fr_contract, contactListFragment).commit();
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        finish();
    }
}
