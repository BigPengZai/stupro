package io.agore.openvcall.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;

import java.util.ArrayList;

import io.agore.openvcall.model.Message;


public class InChannelMessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> mMsglist;

    private final Context mContext;
    protected final LayoutInflater mInflater;

    public InChannelMessageListAdapter(Context context, ArrayList<Message> list) {
        mContext = context;
        mInflater = ((Activity) context).getLayoutInflater();
        mMsglist = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMsglist.get(position).getType() == 1) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = mInflater.inflate(R.layout.in_channel_message, parent, false);
            return new MessageHolder(v);
        } else {
            View v = mInflater.inflate(R.layout.in_channel_message2, parent, false);
            return new MessageHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message msg = mMsglist.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                MessageHolder myHolder = (MessageHolder) holder;
                myHolder.msgContent.setText(msg.getContent());
                break;
            case 0:
                MessageHolder2 myHolder2 = (MessageHolder2) holder;
                myHolder2.msgContent.setText(msg.getContent());
                switch (msg.getType()) {
                    case 0:
                        myHolder2.identity.setText("老师");
                        break;
                    case 2:
                        myHolder2.identity.setText("教学兼课");
                        break;
                    case 3:
                        myHolder2.identity.setText("课程顾问");
                        break;
                    case 4:
                        myHolder2.identity.setText("班主任");
                        break;
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMsglist.size();
    }

    @Override
    public long getItemId(int position) {
        return mMsglist.get(position).hashCode();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        public TextView msgContent;

        public MessageHolder(View v) {
            super(v);
            msgContent = (TextView) v.findViewById(R.id.msg_content);
        }
    }

    public class MessageHolder2 extends RecyclerView.ViewHolder {
        public TextView msgContent;
        public TextView identity;

        public MessageHolder2(View v) {
            super(v);
            msgContent = (TextView) v.findViewById(R.id.msg_content);
            identity = (TextView) v.findViewById(R.id.identity);
        }
    }

}
