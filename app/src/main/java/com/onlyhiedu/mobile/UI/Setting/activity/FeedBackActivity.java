package com.onlyhiedu.mobile.UI.Setting.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.DialogListener;
import com.onlyhiedu.mobile.Utils.DialogUtil;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/3/3.
 */

public class FeedBackActivity extends SimpleActivity implements View.OnClickListener {
    @BindView(R.id.btn_submit)
    Button mBtn_Submit;
    @Override
    protected int getLayout() {
        return R.layout.activity_feed;
    }

    @Override
    protected void initEventAndData() {
        initListener();
    }

    private void initListener() {
        mBtn_Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                DialogUtil.showOnlyAlert(this,
                        "提示"
                        , "提交成功"
                        , ""
                        , ""
                        , true, true, new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {

                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        }
                );
                break;
        }
    }
}
