package io.agore.openvcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.onlyhiedu.mobile.R;
import io.agore.openvcall.model.ConstantApp;


public class RoomActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RoomActivity.class.getSimpleName();
    private Button mMBut_stu;
    private Button mBut_Guwen;
    private Button mMBut_jiazhang;
    private EditText mV_channel;
    private EditText mMV_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_room);
    }

    protected void initUIandEvent() {
        mV_channel = (EditText) findViewById(R.id.channel_name);
        mMV_uid = (EditText) findViewById(R.id.channel_uid);
        mMBut_stu = (Button) findViewById(R.id.but_stu);
        mBut_Guwen = (Button) findViewById(R.id.but_guwen);
        mMBut_jiazhang = (Button) findViewById(R.id.but_jiazhang);
        mMBut_stu.setOnClickListener(this);
        mBut_Guwen.setOnClickListener(this);
        mMBut_jiazhang.setOnClickListener(this);
        mMBut_stu.setEnabled(false);
        mV_channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                mBut_Guwen.setEnabled(!isEmpty);
                mMBut_jiazhang.setEnabled(!isEmpty);
            }
        });
        mMV_uid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean isEmpty = TextUtils.isEmpty(editable.toString());
                if (editable.toString().equals("123")) {
                    mMBut_stu.setEnabled(false);
                } else {
                    mMBut_stu.setEnabled(!isEmpty);
                }
            }
        });


        Spinner encryptionSpinner = (Spinner) findViewById(R.id.encryption_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.encryption_mode_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encryptionSpinner.setAdapter(adapter);

        encryptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSettings().mEncryptionModeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        encryptionSpinner.setSelection(vSettings().mEncryptionModeIndex);

        String lastChannelName = vSettings().mChannelName;
        if (!TextUtils.isEmpty(lastChannelName)) {
            mV_channel.setText(lastChannelName);
            mV_channel.setSelection(lastChannelName.length());
        }

        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String lastEncryptionKey = vSettings().mEncryptionKey;
        if (!TextUtils.isEmpty(lastEncryptionKey)) {
            v_encryption_key.setText(lastEncryptionKey);
        }
    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                forwardToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void forwardToRoom() {
//        yuvEnhancer = new AgoraYuvEnhancer(this);
//        int i1 = yuvEnhancer.StartPreProcess();
//        Log.d(TAG, "打开状态："+i1);

        String channel = mV_channel.getText().toString();
        vSettings().mChannelName = channel;
        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String encryption = v_encryption_key.getText().toString();
        vSettings().mEncryptionKey = encryption;
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
        i.putExtra(ConstantApp.ACTION_KEY_UID, mMV_uid.getText().toString());
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, encryption);
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, getResources().getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
        startActivity(i);
    }

    public void forwardToSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_stu:
                forwardToRoom();
                break;
            case R.id.but_guwen:
                guwenToRoom();
                break;
            case R.id.but_jiazhang:
                guwenToRoom();
                break;
        }
    }

    private void guwenToRoom() {
        String channel = mV_channel.getText().toString();
        vSettings().mChannelName = channel;
        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String encryption = v_encryption_key.getText().toString();
        vSettings().mEncryptionKey = encryption;
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
        i.putExtra(ConstantApp.ACTION_KEY_UID, "123");
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, encryption);
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, getResources().getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
        startActivity(i);
    }
}
