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
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.User;
import com.example.mrwen.view.OnDeleteClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/3/23.
 */

public class RecyclerCourseNoticeAdapter extends BaseRecyclerViewAdapter<Notice,RecyclerCourseNoticeAdapter.MyNoticesViewHolder> {
    public RecyclerCourseNoticeAdapter(ArrayList<Notice> data) {
        super(data);
    }
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public MyNoticesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_course_notice, parent, false);

        return new MyNoticesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyNoticesViewHolder holder, int position) {
        final Notice notice = data.get(position);

        holder.tv_course_notice_name.setText( notice.getName());
        holder.tv_course_notice_content.setText(notice.getContent() );
        holder.tv_course_notice_number.setText("浏览"+notice.getNumber()+"次");
        holder.tv_course_nitice_date.setText(TimeUtils.ExactFormat(notice.getDate()));
        holder.layout_course_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, notice);
            }
        });
        holder.layout_delete_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(notice.getId());
            }
        });


    }
    public void remove(int id){
        for (Iterator iter=data.iterator(); iter.hasNext();) {
            Notice notice=(Notice) iter.next();
            if (notice.getId()==id)
                iter.remove();
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class MyNoticesViewHolder extends RecyclerView.ViewHolder{
        TextView tv_course_notice_name;
        TextView tv_course_notice_content;
        TextView tv_course_notice_number;
        TextView tv_course_nitice_date;
        LinearLayout layout_course_notice;
        LinearLayout layout_delete_notice;
        public MyNoticesViewHolder(View itemView) {
            super(itemView);
            tv_course_notice_name = ButterKnife.findById(itemView, R.id.tv_course_notice_name);
            tv_course_notice_content = ButterKnife.findById(itemView, R.id.tv_course_notice_content);
            tv_course_notice_number = ButterKnife.findById(itemView, R.id.tv_course_notice_number);
            tv_course_nitice_date = ButterKnife.findById(itemView, R.id.tv_course_nitice_date);
            layout_course_notice = ButterKnife.findById(itemView, R.id.layout_course_notice);
            layout_delete_notice = ButterKnife.findById(itemView, R.id.layout_delete_notice);
        }
    }
}