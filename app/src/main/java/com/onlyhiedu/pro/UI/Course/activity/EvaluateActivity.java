package com.onlyhiedu.pro.UI.Course.activity;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.Model.bean.StarContentList;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Course.persenter.EvaluatePresenter;
import com.onlyhiedu.pro.UI.Course.persenter.contract.EvaluateContract;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EvaluateActivity extends BaseActivity<EvaluatePresenter> implements EvaluateContract.View {

    public static final String courseUuidKey = "courseUuid";

    private String courseUuid;

    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.edit)
    EditText mEdit;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private ProgressDialog mDialog;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_evaluate;
    }

    @Override
    protected void initView() {
        courseUuid = getIntent().getStringExtra(courseUuidKey);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mPresenter.getStarContextList((int) (v + 0.5));
            }
        });
    }

    @Override
    protected void initData() {
    }


    @Override
    public void showStarContextList(List<StarContentList> data) {
        Toast.makeText(this, "data:" + data, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void saveAppraiseSuccess(String message) {
        mDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveAppraiseFailure(String message) {
        mDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                if (mDialog == null) {
                    mDialog = ProgressDialog.show(this, null, "正在提交...", true, true);
                } else {
                    mDialog.show();
                }
                int num = (int) (mRatingBar.getRating() + 0.5);
                Log.d("xwc", num + "");
                mPresenter.SaveAppraise(num, "", mEdit.getText().toString(), courseUuid);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
