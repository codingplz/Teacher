package com.example.mrwen.adapter;

import android.content.Intent;
import android.provider.Contacts;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.User;
import com.example.mrwen.view.OnUserInfoClickListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;


import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by fate on 2016/12/9.
 */

public class RecyclerDiscoverAdapter extends BaseRecyclerViewAdapter<Issue, RecyclerDiscoverAdapter.MyViewHolder> {


    private OnAnswerClickListener onAnswerClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    private OnUserInfoClickListener mOnUserInfoClickListener;
    public RecyclerDiscoverAdapter(ArrayList<Issue> data) {
        super(data);
    }

    public void setOnAnswerClickListener(OnAnswerClickListener onAnswerClickListener) {
        this.onAnswerClickListener = onAnswerClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_dicover, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Issue issue = data.get(position);
        holder.tvTitle.setText(issue.getTitle());
        holder.tvDetail.setText(issue.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(v, (Issue) v.getTag());
            }
        });
        if (issue.isAnonymous()) {
            holder.tvName.setText("匿名提问");
            Glide.with(UiUtils.getContext())
                    .load(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(holder.ivImage);
        }else {
            final User user = issue.getUser();
            holder.tvName.setText(user.getNickname());
            Glide.with(UiUtils.getContext()).load(UiUtils.getContext().getResources().getString(R.string.baseURL)+user.getImageURL()).placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(holder.ivImage);
            holder.llAsker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnUserInfoClickListener!=null)
                        mOnUserInfoClickListener.onUserInfoClickListener("s"+ user.getId());
                }
            });
        }

        holder.tvNumber.setText(issue.getAnswerNumber()+"人回答");
        holder.itemView.setTag(issue);
        holder.ibAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAnswerClickListener != null)
                    onAnswerClickListener.onAnswerClick(issue.getId());
            }
        });
    }





    public interface OnAnswerClickListener {
        void onAnswerClick(int iid);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ExpandableTextView tvDetail;
        TextView tvNumber;
        ImageView ibShare;
        ImageView ibAnswer;
        View itemView;
        ImageView ivImage;
        TextView tvName;
        LinearLayout llAsker;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = ButterKnife.findById(itemView,R.id.tv_name);
            ivImage = ButterKnife.findById(itemView,R.id.iv_image);
            tvTitle = ButterKnife.findById(itemView, R.id.tv_title);
            tvDetail = ButterKnife.findById(itemView, R.id.etv_question_content);
            tvNumber = ButterKnife.findById(itemView, R.id.tv_discover_number);
            ibShare = ButterKnife.findById(itemView, R.id.ib_share);
            ibAnswer = ButterKnife.findById(itemView, R.id.ib_answer);
            llAsker = ButterKnife.findById(itemView,R.id.ll_asker);
            this.itemView = itemView;
        }
    }
}
