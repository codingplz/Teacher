package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.TimeUtils;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.User;
import com.example.mrwen.view.OnDeleteClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2017/2/18.
 */

public class RecyclerMyAnswersAdapter extends BaseRecyclerViewAdapter<Answer,RecyclerMyAnswersAdapter.MyAnswersViewHolder> {
    public RecyclerMyAnswersAdapter(ArrayList<Answer> data) {
        super(data);
    }
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnUserInfoClickListener mOnUserInfoClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public MyAnswersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_my_answer, parent, false);

        return new MyAnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAnswersViewHolder holder, int position) {
        final Answer answer = data.get(position);
        Issue issue = answer.getIssue();
        final User user = issue.getUser();
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + user.getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .into(holder.ivImage);
        holder.tvName.setText( user.getNickname());
        holder.tvTime.setText(TimeUtils.Format(answer.getTime()) );
        holder.tvContent.setText(answer.getContent());
        holder.tvTitle.setText(issue.getTitle());
        holder.tvAgree.setText(answer.getAgree()+"人赞同");
        holder.itemView.setTag(answer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (Answer) v.getTag());
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(answer.getId());
            }
        });
        holder.llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnUserInfoClickListener!=null)
                    mOnUserInfoClickListener.onUserInfoClickListener("s"+user.getId());
            }
        });

    }
    public void remove(int id){
        for (Answer answer : data) {
            if (answer.getId()==id)
                data.remove(answer);
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class MyAnswersViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvContent;
        TextView tvAgree;
        TextView tvName;
        ImageView ivImage;
        TextView tvTime;
        ImageButton ibDelete;
        LinearLayout llUser;
        public MyAnswersViewHolder(View itemView) {
            super(itemView);
            tvTitle = ButterKnife.findById(itemView, R.id.tv_issue_title);
            tvContent = ButterKnife.findById(itemView, R.id.tv_answer_content);
            tvAgree = ButterKnife.findById(itemView, R.id.tv_agree);
            tvName = ButterKnife.findById(itemView, R.id.tv_answer_name);
            ivImage = ButterKnife.findById(itemView, R.id.iv_answer_image);
            tvTime = ButterKnife.findById(itemView, R.id.tv_time);
            ibDelete = ButterKnife.findById(itemView, R.id.ib_delete);
            llUser = ButterKnife.findById(itemView, R.id.ll_answer);
        }
    }
}
