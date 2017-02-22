package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideRoundTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Info;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2017/2/10.
 */

public class RecyclerQueryResultAdapter extends BaseRecyclerViewAdapter<Info, RecyclerQueryResultAdapter.MyViewHolder> {
    private int type;
    private final static int TYPE_FRIENDS = 0;
    private final static int TYPE_STRANGES = 1;

    public void setOnAddFriendListener(OnAddFriendListener onAddFriendListener) {
        mOnAddFriendListener = onAddFriendListener;
    }

    private OnAddFriendListener mOnAddFriendListener;

    public RecyclerQueryResultAdapter(ArrayList<Info> data, int type) {
        super(data);
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_query_result, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecyclerQueryResultAdapter.this.mListener != null)
                    RecyclerQueryResultAdapter.this.mListener.onItemClick(v, (Info) v.getTag());
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Info info = data.get(position);
        holder.rootView.setTag(info);
        switch (type) {
            case TYPE_STRANGES:
                holder.name.setText(info.getNickname() + "(" + info.getUsername() + ")");
                holder.addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAddFriendListener != null)
                            mOnAddFriendListener.onAddFriend(info, holder);
                    }
                });
                break;
            case TYPE_FRIENDS:
                holder.name.setText("[" + info.getRemark() + "]" + info.getNickname() + "(" + info.getUsername() + ")");
                holder.flAdd.setVisibility(View.GONE);
                break;
        }
        holder.signiture.setText(info.getSignature());
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + info.getImageURL())
                .dontAnimate()
                .transform(new GlideRoundTransform(UiUtils.getContext(),5))
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .into(holder.image);
        holder.identity.setText(StaticInfo.getIdentity(info.getUid()));
    }



    public interface OnAddFriendListener {
        void onAddFriend(Info info, MyViewHolder holder);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView signiture;
        ImageView image;
        TextView identity;
        TextView addFriend;
        TextView added;
        ProgressBar adding;
        View rootView;
        FrameLayout flAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            signiture = ButterKnife.findById(itemView, R.id.tv_contact_signiture);
            name = ButterKnife.findById(itemView, R.id.tv_contact_name);
            image = ButterKnife.findById(itemView, R.id.iv_contact_image);
            identity = ButterKnife.findById(itemView, R.id.tv_identity);
            addFriend = ButterKnife.findById(itemView, R.id.tv_add_friend);
            added = ButterKnife.findById(itemView, R.id.tv_added);
            adding = ButterKnife.findById(itemView, R.id.pg_add_friend);
            flAdd = ButterKnife.findById(itemView, R.id.fl_add);
        }
    public void showAdding() {
        adding.setVisibility(View.VISIBLE);
       addFriend.setVisibility(View.GONE);
        added.setVisibility(View.GONE);
    }

    public void showAdded() {
        adding.setVisibility(View.GONE);
        addFriend.setVisibility(View.GONE);
        added.setVisibility(View.VISIBLE);
    }

    public void showAddFriend() {
        adding.setVisibility(View.GONE);
        addFriend.setVisibility(View.VISIBLE);
        added.setVisibility(View.GONE);
    }
    }
}
