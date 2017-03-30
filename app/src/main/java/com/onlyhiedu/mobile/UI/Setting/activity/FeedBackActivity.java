package com.onlyhiedu.mobile.UI.Setting.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Setting.presenter.FeedBackPresenter;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.FeedBackContract;
import com.onlyhiedu.mobile.UI.Setting.presenter.contract.UpdataContract;

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
        Log.d(TAG, "msg:" + msg);
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
                mBt_Finish.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }
}
