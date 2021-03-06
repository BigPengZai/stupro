package com.onlyhiedu.pro.UI.Setting.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Setting.presenter.FeedBackPresenter;
import com.onlyhiedu.pro.UI.Setting.presenter.contract.FeedBackContract;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/3.
 * 意见反馈
 */

public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackContract.View {

    public static final String TAG = FeedBackActivity.class.getSimpleName();

    @BindView(R.id.bt_finish)
    TextView mBt_Finish;
    @BindView(R.id.et_feed_back)
    EditText mEt_Feed_Back;
    private String mContent;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_feed;
    }

    @Override
    protected void initView() {
        setToolBar("意见反馈");
        initButState();
    }

    @OnClick(R.id.bt_finish)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_finish:
                mContent = mEt_Feed_Back.getText().toString();
                if (mContent != null) {
                    Log.d(TAG, "mContent:" + mContent);
                    mPresenter.sendFeedBack(mContent);
                }
                break;
        }
    }

    @Override
    public void showFeedBackSuccess(String msg) {
        Log.d(TAG, "msg:" + msg);
        if (msg!=null&&msg.equals("成功")) {
            Toast.makeText(this,"意见反馈成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private void initButState() {
        mEt_Feed_Back.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                mBt_Finish.setEnabled(!isEmpty);
                // #F74D64    <color name="c_D2D0D0">#D2D0D0</color>
                mBt_Finish.setTextColor(isEmpty ? Color.parseColor("#D2D0D0"): Color.parseColor("#F74D64"));
            }
        });
    }
}
