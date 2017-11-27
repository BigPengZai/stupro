package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onlyhiedu.mobile.R;

/**
 * Created by xwc on 2017/3/7.
 */

public class InputTextView extends RelativeLayout implements TextWatcher, View.OnClickListener {

    public EditText mEditText;
    private ImageView mImageClose;
    private Button mButton;

    private boolean isPassword;
    private boolean show;

    public InputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_input_text, this, true);

        mEditText = (EditText) findViewById(R.id.edit);
        mImageClose = (ImageView) findViewById(R.id.image_close);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.input_text);
        setShowIcon(localTypedArray.getBoolean(R.styleable.input_text_item_showIcon, false));
        setIcon(localTypedArray.getDrawable(R.styleable.input_text_item_close_icon));
        setHint(localTypedArray.getString(R.styleable.input_text_item_hint_text));
        setInputType(localTypedArray.getInt(R.styleable.input_text_item_InputType, 0));

        mEditText.addTextChangedListener(this);
        mImageClose.setOnClickListener(this);
    }

    public void setShowIcon(boolean showIcon) {
        if (showIcon) {
            mImageClose.setVisibility(VISIBLE);
        } else {
            mImageClose.setVisibility(GONE);
        }
    }

    public void setButton(Button button) {
        mButton = button;
    }

    public void setInputType(int inputType) {
        switch (inputType) {
            case 0:
                break;
            case 1:
                mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 2:
                mEditText.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
                //个人中心 修改密码maxLength 20
                mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                break;
        }

    }


    private void setIcon(Drawable dra) {
        mImageClose.setImageDrawable(dra);
    }

    public String getEditText() {
        return mEditText.getText().toString();
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().length() != 0) {
            if (mButton != null) {
                mButton.setEnabled(true);
            }
            mImageClose.setVisibility(VISIBLE);
        } else {
            if (mButton != null) {
                mButton.setEnabled(false);
            }
            mImageClose.setVisibility(GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public void setInputEnable(boolean enable) {
        mEditText.setEnabled(enable);
    }


    public void setPassword(boolean password) {
        isPassword = password;
    }

    public void pwdTextShow() {
        mEditText.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        mImageClose.setImageResource(R.mipmap.visible);
        initEditText();
    }

    public void pwdTextHint() {
        mEditText.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        mImageClose.setImageResource(R.mipmap.ic_pwd_hide);
        initEditText();
    }

    private void initEditText() {
        CharSequence s = mEditText.getText();
        if (s instanceof Spannable) {
            Spannable spanText = (Spannable) s;
            Selection.setSelection(spanText, s.length());
        }
    }

    @Override
    public void onClick(View view) {
        if (isPassword) {
            if (show) {
                show = false;
                pwdTextShow(); 
            } else {
                show = true;
                pwdTextHint();
            }
        } else {
            mEditText.setText("");
        }
    }

    public void clean() {
        mEditText.setText("");
    }


    public EditText getEditTextView() {
        return mEditText;
    }

}
