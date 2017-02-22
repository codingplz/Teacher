package com.example.mrwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mrwen.activity.R;
import com.example.mrwen.otherclass.LessonPartInfo;

import java.util.List;

/**
 * Created by mrwen on 2016/12/24.
 */

public class LessonAdapter extends BaseExpandableListAdapter {

    private String[] lessonItem =new String[]{"课时信息","视频查看","课后习题","课时统计"};

    private Context context;
    private List<LessonPartInfo> list;

    public List<LessonPartInfo> getList(){
        return list;
    }


    public LessonAdapter(Context context, List<LessonPartInfo> list)
    {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lessonItem.length;
    }

    @Override
    public LessonPartInfo getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lessonItem[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.lesson_group, null);
            groupHolder = new LessonAdapter.GroupHolder();
            groupHolder.lessonIndex = (TextView)convertView.findViewById(R.id.tv_lesson_index);
            groupHolder.lessonName = (TextView)convertView.findViewById(R.id.tv_lessonName);
            //groupHolder.buttonStatus= (TextView)convertView.findViewById(R.id.bt_chapter);
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (LessonAdapter.GroupHolder)convertView.getTag();
        }

        groupHolder.lessonIndex.setText(list.get(groupPosition).getIndex());
        groupHolder.lessonName.setText(list.get(groupPosition).getName());
        //groupHolder.buttonStatus.setText(buttonStatus[groupPosition]);
//        if(list.get(groupPosition).getIsUpload()==1){
//            groupHolder.lessonIndex.setTextColor(Color.parseColor("#000000"));
//            groupHolder.lessonName.setTextColor(Color.parseColor("#000000"));
//        }
//        else {
//            groupHolder.lessonIndex.setTextColor(Color.parseColor("#999999"));
//            groupHolder.lessonName.setTextColor(Color.parseColor("#999999"));
//        }
        groupHolder.lessonIndex.setTextColor(Color.parseColor("#000000"));
        groupHolder.lessonName.setTextColor(Color.parseColor("#000000"));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LessonAdapter.ItemHolder itemHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.lesson_item, null);
            itemHolder = new LessonAdapter.ItemHolder();
            itemHolder.lessonItem = (TextView)convertView.findViewById(R.id.tv_lessonItem);
            convertView.setTag(itemHolder);
        }

        else
        {
            itemHolder = (LessonAdapter.ItemHolder)convertView.getTag();
        }
        itemHolder.lessonItem.setText(lessonItem[childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder
    {
        public TextView lessonIndex;
        public TextView lessonName;
        //public TextView buttonStatus;

    }

    class ItemHolder
    {
        public TextView lessonItem;

    }
}
