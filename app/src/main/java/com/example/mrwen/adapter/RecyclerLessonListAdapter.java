package com.example.mrwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Lesson;
import com.example.mrwen.otherclass.ChapterPartInfo;
import com.example.mrwen.otherclass.LessonPartInfo;
import com.example.mrwen.view.OnChapterListMenuListener;
import com.example.mrwen.view.OnGetIdListener;
import com.example.mrwen.view.OnLessonDeleteListener;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/3/6.
 */

public class RecyclerLessonListAdapter extends RecyclerView.Adapter<RecyclerLessonListAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<LessonPartInfo> list;
    protected OnRecyclerViewItemClickListener mListener;
    protected OnLessonDeleteListener lessonDeleteListener;
    public List<LessonPartInfo> getList(){
        return list;
    }

    public RecyclerLessonListAdapter(Context context, List<LessonPartInfo> list)
    {
        this.context = context;
        this.list=list;
    }

    public void setOnClickListener(OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }
    public void setOnLessonDeleteListener(OnLessonDeleteListener listener){
        lessonDeleteListener=listener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LessonPartInfo lesson = list.get(position);
//        Glide.with(UiUtils.getContext())
//                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + user.getImageURL())
//                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
//                .transform(new GlideCircleTransform(UiUtils.getContext()))
//                .into(holder.ivImage);
        holder.lessonName.setText(lesson.getLessonName());
        holder.lessonIndex.setText(lesson.getIndex());
        //holder.chapterName.setText(lesson.getName());
        holder.lesson.setTag(lesson);
        holder.lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.onItemClick(v,lesson);
            }
        });
        holder.deleteLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonDeleteListener!=null)
                    lessonDeleteListener.onLessonDeleteClick(lesson.getId(),lesson.getLessonName());
            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_lesson_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(cell);

        // 对每一个cell注册点击事件
        cell.setOnClickListener(this);

        // 取消viewHolder的重用机制（很重要）
        myViewHolder.setIsRecyclable(false);

        return myViewHolder;
    }

    @Override
    public void onClick(View v) {

//        int index;
//        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.layout_chapter_list);
//        View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.item_chapter_list_add, (ViewGroup)v, false);
//
//        RecyclerView rv=(RecyclerView)subView.findViewById(R.id.recycler_lesson_list);
//
//
//
//        // 利用cell控件的Tag值来标记cell是否被点击过,因为已经将重用机制取消，cell退出当前界面时就会被销毁，Tag值也就不存在了。
//        // 如果不取消重用，那么将会出现未曾点击就已经添加子视图的效果，再点击的时候会继续添加而不是收回。
//        if (v.findViewById(R.id.layout_chapter_list).getTag() == null) {
//            index = 1;
//        } else {
//            index = (int)v.findViewById(R.id.layout_chapter_list).getTag();
//        }
//
//        // close状态: 添加视图
//        if (index == 1) {
//            linearLayout.addView(subView);
//            subView.setTag(1000);
//            v.findViewById(R.id.layout_chapter_list).setTag(2);
//        } else {
//            // open状态： 移除视图
//            linearLayout.removeView(v.findViewWithTag(1000));
//            v.findViewById(R.id.layout_chapter_list).setTag(1);
//        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView lessonName;
        TextView lessonIndex;
        LinearLayout lesson;
        LinearLayout deleteLesson;


        public MyViewHolder(View itemView) {
            super(itemView);
            lessonName = ButterKnife.findById(itemView, R.id.tv_lesson_list_name);
            lessonIndex = ButterKnife.findById(itemView, R.id.tv_lesson_list_index);
            lesson=ButterKnife.findById(itemView,R.id.layout_lesson);
            deleteLesson=ButterKnife.findById(itemView,R.id.layout_delete_lesson);
        }
    }
}