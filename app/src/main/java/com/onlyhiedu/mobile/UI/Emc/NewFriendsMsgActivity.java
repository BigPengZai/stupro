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

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.adapter.NewFriendsMsgAdapter;
import com.onlyhiedu.mobile.UI.Emc.presenter.NewFriendsMsgPresenter;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.NewFriendsMsgContract;
import com.onlyhiedu.mobile.db.InviteMessgeDao;
import com.onlyhiedu.mobile.domain.InviteMessage;

import java.util.List;

/**
 * Application and notification
 * 申请与通知
 */
public class NewFriendsMsgActivity extends EaseBaseActivity2<NewFriendsMsgPresenter> implements NewFriendsMsgContract.View {


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.em_activity_new_friends_msg;
    }

    @Override
    protected void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();

        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);
    }


    public void addFriend(String phone) {
        mPresenter.addFriends(phone);
    }


    public void back(View view) {
        finish();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}