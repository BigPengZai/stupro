package com.onlyhiedu.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.util.EMLog;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;

public class CallReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
		if(!DemoHelper.getInstance().isLoggedIn())
		    return;
		//username
		String from = intent.getStringExtra("from");
		//call type
		String type = intent.getStringExtra("type");
		EMLog.d("CallReceiver", "app received a incoming call");
	}

}