package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.view.FullyLinearLayoutManager;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2017/2/10.
 */

public class RecyclerQueryContactAdapter extends BaseRecyclerViewAdapter<QueryItem, RecyclerQueryContactAdapter.MyViewHolder> {

    public void setOnAddFriendListener(OnAddFriendListener onAddFriendListener) {
        mOnAddFriendListener = onAddFriendListener;
    }

    private OnAddFriendListener mOnAddFriendListener;
    private OnQueryItemClickListener mOnQueryItemClickListener;

    public void setOnQueryItemClickListener(OnQueryItemClickListener onQueryItemClickListener) {
        mOnQueryItemClickListener = onQueryItemClickListener;
    }

    public RecyclerQueryContactAdapter(ArrayList<QueryItem> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_query_contact, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final QueryItem item = data.get(position);
        holder.rootView.setTag(item);
        holder.title.setText(item.getTitle());
        holder.results.setLayoutManager(new FullyLinearLayoutManager(UiUtils.getContext()));
        RecyclerQueryResultAdapter adapter = new RecyclerQueryResultAdapter(item.getInfos(), item.getType());
        holder.results.setAdapter(adapter);
        adapter.setOnAddFriendListener(new RecyclerQueryResultAdapter.OnAddFriendListener() {
            @Override
            public void onAddFriend(Info info, RecyclerQueryResultAdapter.MyViewHolder holder) {
                if (mOnAddFriendListener!=null)
                    mOnAddFriendListener.onAddFriend(info,holder);
            }
        });
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Info>() {
            @Override
            public void onItemClick(View v, Info data) {
                if (mOnQueryItemClickListener!=null)
                    mOnQueryItemClickListener.onQueryItemClick(v,data);
            }
        });
    }




    public interface OnAddFriendListener {
        void onAddFriend(Info info, RecyclerQueryResultAdapter.MyViewHolder holder);
    }
    public interface OnQueryItemClickListener {
        void onQueryItemClick(View v, Info data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView results;
        View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = ButterKnife.findById(itemView, R.id.tv_title);
            results = ButterKnife.findById(itemView,R.id.recycler_query);
        }
    }
}
