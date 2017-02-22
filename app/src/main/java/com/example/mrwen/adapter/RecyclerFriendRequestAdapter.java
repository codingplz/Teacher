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
import com.example.mrwen.bean.FriendRequest;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by fate on 2017/2/10.
 */

public class RecyclerFriendRequestAdapter extends BaseRecyclerViewAdapter<FriendRequest, RecyclerFriendRequestAdapter.MyViewHolder> {


    public void setOnAddFriendListener(OnAddFriendListener onAddFriendListener) {
        mOnAddFriendListener = onAddFriendListener;
    }

    private OnAddFriendListener mOnAddFriendListener;

    public RecyclerFriendRequestAdapter(ArrayList<FriendRequest> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_friend_request, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecyclerFriendRequestAdapter.this.mListener != null)
                    RecyclerFriendRequestAdapter.this.mListener.onItemClick(v, (FriendRequest) v.getTag());
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final FriendRequest info = data.get(position);
        holder.rootView.setTag(info);

        holder.name.setText(info.getNickname());
                holder.addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAddFriendListener != null)
                            mOnAddFriendListener.onAddFriend(info, holder);
                    }
                });
        holder.signiture.setText(info.getMessage());
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + info.getImageURL())
                .dontAnimate()
                .transform(new GlideRoundTransform(UiUtils.getContext(),5))
                .placeholder(R.drawable.default_photo)
                .into(holder.image);
        holder.identity.setText(StaticInfo.getIdentity(info.getUidFrom()));
    }



    public interface OnAddFriendListener {
        void onAddFriend(FriendRequest info, MyViewHolder holder);
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
       addFriend.setVisibility(View.INVISIBLE);
        added.setVisibility(View.INVISIBLE);
    }

    public void showAdded() {
        adding.setVisibility(View.INVISIBLE);
        addFriend.setVisibility(View.INVISIBLE);
        added.setVisibility(View.VISIBLE);
    }

    public void showAddFriend() {
        adding.setVisibility(View.INVISIBLE);
        addFriend.setVisibility(View.VISIBLE);
        added.setVisibility(View.INVISIBLE);
    }
    }
}
