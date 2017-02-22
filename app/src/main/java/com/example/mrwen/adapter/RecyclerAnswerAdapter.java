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
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Info;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2016/12/9.
 */

public class RecyclerAnswerAdapter extends BaseRecyclerViewAdapter<Answer, RecyclerAnswerAdapter.MyViewHolder> {
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void setOnAgreeClickListener(OnAgreeClickListener onAgreeClickListener) {
        mOnAgreeClickListener = onAgreeClickListener;
    }
    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnAgreeClickListener mOnAgreeClickListener;

    public RecyclerAnswerAdapter(ArrayList<Answer> data) {
        super(data);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_answer, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Answer answer = data.get(position);
        final Info answerer = answer.getAnswerer();
        if (answer.isAnonymous()) {
            holder.tvName.setText("匿名回答");
            Glide.with(UiUtils.getContext())
                    .load(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(holder.image);
        } else {
            String identity = "";
            switch (answerer.getUid().charAt(0)) {
                case 's':
                    identity = "学生";
                    break;
                case 't':
                    identity = "教师";
                    break;
                case 'c':
                    identity = "大学生";
                    break;
                case 'a':
                    identity = "家长";
                    break;
                default:
                    break;
            }

            Glide.with(UiUtils.getContext())
                    .load(UiUtils.getContext().getResources().getString(R.string.baseURL)+ answerer.getImageURL())
                    .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(holder.image);
            String nickname = answerer.getNickname();
            holder.tvName.setText(nickname + "  " + identity);
            holder.llAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnUserInfoClickListener!=null)
                        mOnUserInfoClickListener.onUserInfoClickListener(answerer.getUid());
                }
            });
        }

        holder.tvAnswer.setText(answer.getContent());
        holder.tvTime.setText(mFormat.format(answer.getTime()));
        holder.tvAgree.setText(answer.getAgree() + "人赞同");
        holder.ibAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAgreeClickListener != null) {
                    mOnAgreeClickListener.onAgreeClick(answer.getId());
                    increment(answer);
                    holder.ibAgree.setEnabled(false);
                }
            }
        });
    }

    private void increment(Answer answer) {
        answer.setAgree(answer.getAgree() + 1);
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();

            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName;
        TextView tvAnswer;
        TextView tvAgree;
        TextView tvTime;
        ImageButton ibAgree;
        LinearLayout llAnswer;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = ButterKnife.findById(itemView, R.id.iv_answer_image);
            tvName = ButterKnife.findById(itemView, R.id.tv_answer_name);
            tvAnswer = ButterKnife.findById(itemView, R.id.tv_answer_content);
            tvAgree = ButterKnife.findById(itemView, R.id.tv_agree);
            tvTime = ButterKnife.findById(itemView, R.id.tv_time);
            ibAgree = ButterKnife.findById(itemView, R.id.ib_agree);
            llAnswer = ButterKnife.findById(itemView, R.id.ll_answer);
        }
    }

    public interface OnAgreeClickListener {
        void onAgreeClick(int aid);
    }
}
