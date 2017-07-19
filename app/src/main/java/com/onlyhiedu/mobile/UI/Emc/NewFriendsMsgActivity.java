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

import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.adapter.NewFriendsMsgAdapter;
import com.onlyhiedu.mobile.UI.Emc.base.EaseRxBaseActivity;
import com.onlyhiedu.mobile.UI.Emc.presenter.NewFriendsMsgPresenter;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.NewFriendsMsgContract;
import com.onlyhiedu.mobile.db.InviteMessgeDao;
import com.onlyhiedu.mobile.domain.InviteMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Application and notification
 * 申请与通知
 */
public class NewFriendsMsgActivity extends EaseRxBaseActivity<NewFriendsMsgPresenter> implements NewFriendsMsgContract.View {


    @BindView(R.id.list)
    ListView mList;


    private InviteMessgeDao mDao;
    private List<InviteMessage> mMsgs;

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
        mDao = new InviteMessgeDao(this);
        mMsgs = mDao.getMessagesList();

        List<String> IMNames = new ArrayList<>();
        for (int i = 0; i < mMsgs.size(); i++) {
            IMNames.add(mMsgs.get(i).getFrom());
        }
        if (IMNames.size() > 0) {
            mPresenter.getNewFriends(IMNames);
        } else {
            init();
        }

    }

    private void init() {
        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, mMsgs);
        mList.setAdapter(adapter);
        mDao.saveUnreadMessageCount(0);
    }

    @Override
    public void getNewFriendsSuccess(IMAllUserInfo data) {
        for (int i = 0; i < data.list.size(); i++) {
            mMsgs.get(i).iconurl = data.list.get(i).iconurl;
            mMsgs.get(i).phone = data.list.get(i).phone;
            mMsgs.get(i).userName = data.list.get(i).userName;
        }
        init();
    }


    public void back(View view) {
        finish();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
