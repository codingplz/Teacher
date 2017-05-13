package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.otherclass.ChapterPartInfo;
import com.example.mrwen.view.OnChapterListClickListener;
import com.example.mrwen.view.OnChapterListMenuListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/3/6.
 */

public class RecyclerChapterListAdapter extends RecyclerView.Adapter<RecyclerChapterListAdapter.MyViewHolder> implements View.OnClickListener {

    private List<ChapterPartInfo> list=new ArrayList<>();
    protected OnChapterListClickListener mListener;
    private OnChapterListMenuListener menuListener;

    public void setOnChapterListClickListener(OnChapterListClickListener listener) {
        mListener = listener;
    }

    public void setOnClickListener(OnChapterListMenuListener listener) {
        menuListener = listener;
    }

    public List<ChapterPartInfo> getList(){
        return list;
    }

    public void setData(ArrayList<ChapterPartInfo> data) {
        this.list = data;
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ChapterPartInfo chapter = list.get(position);
//        Glide.with(UiUtils.getContext())
//                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + user.getImageURL())
//                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
//                .transform(new GlideCircleTransform(UiUtils.getContext()))
//                .into(holder.ivImage);
        holder.chapterIndex.setText(chapter.getIndex());
        holder.chapterName.setText(chapter.getName());
        if(chapter.getIsSigned()==0){
            holder.chapterIndex.setTextColor(android.graphics.Color.parseColor("#999999"));
            holder.chapterName.setTextColor(android.graphics.Color.parseColor("#999999"));
        }
        holder.itemView.setTag(chapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onChapterListClick(v, (ChapterPartInfo) v.getTag(),position);
            }
        });
        holder.addLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuListener!=null)
                    menuListener.onChapterListMenuListener(chapter.getId(),v,chapter.getIsSigned(),chapter);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_chapter_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(cell);

        // 对每一个cell注册点击事件
        cell.setOnClickListener(this);

        // 取消viewHolder的重用机制（很重要）
        myViewHolder.setIsRecyclable(false);

        return myViewHolder;
    }

    @Override
    public void onClick(View v) {
//        Log.i("onclickkkk",v.toString());
//        int index;
//        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.layout_chapter_list);
//        View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.item_chapter_list_add, (ViewGroup)v, false);
//        retrofitGetLessonInfo();
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
        TextView chapterIndex;
        TextView chapterName;
        LinearLayout addLesson;
        LinearLayout ll;


        public MyViewHolder(View itemView) {
            super(itemView);
            chapterIndex = ButterKnife.findById(itemView, R.id.tv_chapterIndex);
            chapterName = ButterKnife.findById(itemView, R.id.tv_chapterName);
            addLesson=ButterKnife.findById(itemView, R.id.layout_add_lesson);
            ll=ButterKnife.findById(itemView,R.id.layout_chapter_list_expand);
        }
    }

}