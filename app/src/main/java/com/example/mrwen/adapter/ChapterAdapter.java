package com.example.mrwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mrwen.activity.R;
import com.example.mrwen.otherclass.ChapterPartInfo;

import java.util.List;

/**
 * Created by mrwen on 2016/12/24.
 */

public class ChapterAdapter extends BaseExpandableListAdapter {

    private String[] chapterItem=new String[]{"单元信息","单元测试","单元统计","查看课时"};

    private Context context;
    private List<ChapterPartInfo> list;

    public List<ChapterPartInfo> getList(){
        return list;
    }


    public ChapterAdapter(Context context, List<ChapterPartInfo> list)
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
        return chapterItem.length;
    }

    @Override
    public ChapterPartInfo getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return chapterItem[childPosition];
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
            convertView = LayoutInflater.from(context).inflate(R.layout.chapter_group, null);
            groupHolder = new ChapterAdapter.GroupHolder();
            groupHolder.chapterNumber = (TextView)convertView.findViewById(R.id.tv_chapterNumber);
            groupHolder.chapterName= (TextView)convertView.findViewById(R.id.tv_chapterName);
            //groupHolder.buttonStatus= (TextView)convertView.findViewById(R.id.bt_chapter);
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (ChapterAdapter.GroupHolder)convertView.getTag();
        }

        groupHolder.chapterNumber.setText(list.get(groupPosition).getIndex());
        groupHolder.chapterName.setText(list.get(groupPosition).getName());
        //groupHolder.buttonStatus.setText(buttonStatus[groupPosition]);
        if(list.get(groupPosition).getIsSigned()==1){
            groupHolder.chapterNumber.setTextColor(Color.parseColor("#000000"));
            groupHolder.chapterName.setTextColor(Color.parseColor("#000000"));
        }
        else {
            groupHolder.chapterNumber.setTextColor(Color.parseColor("#999999"));
            groupHolder.chapterName.setTextColor(Color.parseColor("#999999"));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

       ChapterAdapter.ItemHolder itemHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.chapter_item, null);
            itemHolder = new ChapterAdapter.ItemHolder();
            itemHolder.chapterItem = (TextView)convertView.findViewById(R.id.tv_chapterItem);
            convertView.setTag(itemHolder);
        }

        else
        {
            itemHolder = (ChapterAdapter.ItemHolder)convertView.getTag();
        }
        itemHolder.chapterItem.setText(chapterItem[childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder
    {
        public TextView chapterNumber;
        public TextView chapterName;
        //public TextView buttonStatus;

    }

    class ItemHolder
    {
        public TextView chapterItem;

    }
}
