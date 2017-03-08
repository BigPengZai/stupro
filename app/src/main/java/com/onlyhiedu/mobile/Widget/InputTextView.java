package com.onlyhiedu.mobile.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onlyhiedu.mobile.R;

/**
 * Created by xwc on 2017/3/7.
 */

public class InputTextView extends RelativeLayout implements TextWatcher, View.OnClickListener {

    private EditText mEditText;
    private ImageView mImageClose;


    public InputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_input_text, this, true);

        mEditText = (EditText) findViewById(R.id.edit);
        mImageClose = (ImageView) findViewById(R.id.image_close);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.input_text);
        setShowIcon(localTypedArray.getBoolean(R.styleable.input_text_item_showIcon, false));
        setIcon(localTypedArray.getDrawable(R.styleable.input_text_item_close_icon));
        setHint(localTypedArray.getString(R.styleable.input_text_item_hint_text));

        mEditText.addTextChangedListener(this);
        mImageClose.setOnClickListener(this);
    }

    private void setShowIcon(boolean showIcon) {
        if (showIcon) {
            mImageClose.setVisibility(VISIBLE);
        } else {
            mImageClose.setVisibility(GONE);
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
        if (charSequence.toString().length() != 0 && mEditText.hasFocus()) {
            mImageClose.setVisibility(VISIBLE);
        } else {
            mImageClose.setVisibility(GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onClick(View view) {
        mEditText.setText("");
    }
}
