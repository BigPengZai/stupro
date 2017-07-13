/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.onlyhiedu.mobile.UI.Emc;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.base.EaseRxBaseActivity;
import com.onlyhiedu.mobile.UI.Emc.presenter.AddContactPresenter;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.AddContactContract;

import butterknife.BindView;

/**
 * 添加好友
 */
public class AddContactActivity extends EaseRxBaseActivity<AddContactPresenter> implements AddContactContract.View {

    @BindView(R.id.edit_note)
    EditText editText;
    @BindView(R.id.ll_user)
    RelativeLayout searchedUserLayout;
    @BindView(R.id.name)
    TextView nameText;
    @BindView(R.id.search)
    Button searchBtn;
    @BindView(R.id.add_list_friends)
    TextView mTextView;

    String toAddUsername;
    ProgressDialog progressDialog;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.em_activity_add_contact;
    }

    @Override
    protected void initView() {
        String strAdd = getResources().getString(R.string.add_friend);
        mTextView.setText(strAdd);
        String strUserName = getResources().getString(R.string.user_name);
        editText.setHint(strUserName);
    }

    /**
     * search contact
     *
     * @param v
     */
    public void searchContact(View v) {
        final String name = editText.getText().toString();
        String saveText = searchBtn.getText().toString();

        if (getString(R.string.button_search).equals(saveText)) {
            if (TextUtils.isEmpty(name)) {
                new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
                return;
            }

            mPresenter.queryIMUserInfo(name);
        }
    }

    /**
     * add contact
     *
     * @param view
     */
    public void addContact(View view) {
        if (EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (DemoHelper.getInstance().getContactList().containsKey(nameText.getText().toString())) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void showIMUserInfo(IMUserInfo info) {
        //show the userame and add button if user exist
        searchedUserLayout.setVisibility(View.VISIBLE);
        nameText.setText(info.userName);
        toAddUsername = info.imUserName.replace("-", "");
    }

    @Override
    public void showError(String msg) {
        Log.d(Constants.Async, "AddContactActivity -  showError:" + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
