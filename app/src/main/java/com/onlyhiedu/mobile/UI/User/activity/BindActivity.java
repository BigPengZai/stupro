package com.onlyhiedu.mobile.UI.User.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.UIUtils;

public class BindActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
       Button but= (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startLoginActivity(BindActivity.this);
            }
        });

    }
}
