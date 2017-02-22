package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrwen.otherclass.Message;
import com.example.mrwen.activity.R;

import java.util.List;

/**
 * Created by mrwen on 2016/11/1.
 */

public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.ViewHolder>
{
    private List<Message> messagesInfoList;
    private OnItemClickListener mListener;

    public MessageItemAdapter(List<Message> messagesInfoList)
    {
        this.messagesInfoList = messagesInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (Message) itemView.getTag());
            }

        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        Message s = messagesInfoList.get(i);
        viewHolder.bindData(s);
        viewHolder.itemView.setTag(s);
    }

    //获得列表项的数目
    @Override
    public int getItemCount()
    {
        return messagesInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView time;
        private TextView content;
        private ImageView photo;
        public ViewHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvMessageFrom);
            time = (TextView) itemView.findViewById(R.id.tvMessageTime);
            content = (TextView) itemView.findViewById(R.id.tvMessageContent);
            photo=(ImageView)itemView.findViewById(R.id.headPhoto);
        }

        public void bindData(Message s)
        {
            if(s != null) {
                name.setText(s.getName());
                time.setText(s.getTime());
                content.setText(s.getContent());
                photo.setBackgroundResource(s.getImageId());
            }
        }
    }

    //OnItemClickListener接口
    public interface OnItemClickListener
    {
        public void onItemClick(View view,Message data);
    }

    //设置监听
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
