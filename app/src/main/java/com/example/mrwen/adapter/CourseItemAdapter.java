package com.example.mrwen.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;


/**
 * Created by mrwen on 2016/10/30.
 */
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.mrwen.activity.App;
import com.example.mrwen.otherclass.CoursePartInfo;
import com.example.mrwen.activity.R;
import com.example.mrwen.view.OnCourseDeleteListener;


public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.ViewHolder>
{
    private List<CoursePartInfo> courseInfoList;
    private OnItemClickListener mListener;
    private OnCourseDeleteListener onCourseDeleteListener;

    public CourseItemAdapter(List<CoursePartInfo> courseInfoList)
    {
        this.courseInfoList = courseInfoList;
    }

    public void setOnCourseDeleteListener(OnCourseDeleteListener listener){
        this.onCourseDeleteListener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {
        final CoursePartInfo s = courseInfoList.get(i);
        viewHolder.bindData(s);
        viewHolder.itemView.setTag(s);
        viewHolder.courseCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, s);
            }
        });
        viewHolder.deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCourseDeleteListener!=null)
                    onCourseDeleteListener.onCourseDeleteClick(Integer.parseInt(s.getCourseId()),s.getChapterNumber(),s.getCourseName());
            }
        });
    }

    //获得列表项的数目
    @Override
    public int getItemCount()
    {
        return courseInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView courseName;
        private TextView followNumber;
        private TextView uploadChapter;
        private ImageView courseImage;
        private LinearLayout deleteCourse;
        private LinearLayout courseCheck;

        public ViewHolder(View itemView)
        {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.tv_courseName);
            followNumber = (TextView) itemView.findViewById(R.id.tv_followNumber);
            uploadChapter = (TextView) itemView.findViewById(R.id.tv_uploadChapter);
            courseImage=(ImageView)itemView.findViewById(R.id.iv_courseImage);
            deleteCourse=(LinearLayout)itemView.findViewById(R.id.layout_delete_course);
            courseCheck=(LinearLayout)itemView.findViewById(R.id.layout_course_check);
        }

        public void bindData(CoursePartInfo s)
        {
            if(s != null)
                courseName.setText(s.getCourseName());
                followNumber.setText(s.getCourseSubject());
                uploadChapter.setText(s.getCourseChapter());
            if(s.getCourseCover()!=null) {
                Context context = App.getContext();
                Log.i("getCourseCover",s.getCourseCover()+"");
                Glide.with(context).load(context.getResources().getString(R.string.baseURL)+s.getCourseCover()).signature(new StringSignature(UUID.randomUUID().toString())).into(courseImage);
            }
        }
    }

    //OnItemClickListener接口
    public interface OnItemClickListener
    {
        public void onItemClick(View view,CoursePartInfo data);
    }

    //设置监听
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
