package com.example.mrwen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideRoundTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Roster;
import com.example.mrwen.bean.RosterGroup;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.AnimatedExpandableListView;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2016/11/26.
 */

public class ExpandableContactAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    public void setData(ArrayList<RosterGroup> data) {
        this.data = data;
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private ArrayList<RosterGroup> data;

    public ExpandableContactAdapter(ArrayList<RosterGroup> data) {
        this.data = data;
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getRosters().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHoldder viewHoldder;
        if (convertView == null) {
            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_contact_group, null);
            viewHoldder = new GroupViewHoldder();
            viewHoldder.name = ButterKnife.findById(convertView, R.id.tv_group_name);
            viewHoldder.number = ButterKnife.findById(convertView, R.id.tv_contacts_number);
            convertView.setTag(viewHoldder);
        } else {
            viewHoldder = (GroupViewHoldder) convertView.getTag();
        }
        RosterGroup group = data.get(groupPosition);
        viewHoldder.name.setText(group.getName());
        viewHoldder.number.setText(getRealChildrenCount(groupPosition)+"");
        return convertView;
    }

//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (convertView ==null) {
//            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_child, null);
//            TextView title = ButterKnife.findById(convertView, R.id.tv_child_title);
//            Chapter.Lesson lesson = data.get(groupPosition).getLessons().get(childPosition);
//            title.setText(lesson.getTitle());
//        }
//        return convertView;
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_contact_child, null);
            viewHolder = new ChildViewHolder();
            viewHolder.name = ButterKnife.findById(convertView, R.id.tv_contact_name);
            viewHolder.signiture = ButterKnife.findById(convertView, R.id.tv_contact_signiture);
            viewHolder.image = ButterKnife.findById(convertView, R.id.iv_contact_image);
            viewHolder.identity = ButterKnife.findById(convertView, R.id.tv_identity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        Roster roster = data.get(groupPosition).getRosters().get(childPosition);
        viewHolder.name.setText(roster.getRemark());
        viewHolder.signiture.setText(roster.getSignature());
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + roster.getImageURL())
                .dontAnimate()
                .transform(new GlideRoundTransform(UiUtils.getContext(),5))
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .into(viewHolder.image);
        String uid = roster.getUid();
        viewHolder.identity.setText(StaticInfo.getIdentity(uid));
//        Chatter chatter;
//        int presenceNumber = data.get(groupPosition).getPresence().size();
//        if (childPosition < presenceNumber)
//            chatter = data.get(groupPosition).getPresence().get(childPosition);
//        else chatter = data.get(groupPosition).getAbsence().get(childPosition - presenceNumber);
//        ButterKnife.findById(convertView, R.id.ll_presence).setVisibility(chatter.getState() == 0 ? View.VISIBLE : View.GONE);
//        ButterKnife.findById(convertView, R.id.ll_absence).setVisibility(chatter.getState() == 1 ? View.VISIBLE : View.GONE);
        return convertView;
    }



    @Override
    public int getRealChildrenCount(int groupPosition) {
        return data.get(groupPosition).getRosters().size();
    }

    static class ChildViewHolder {
        TextView name;
        TextView signiture;
        ImageView image;
        TextView identity;
    }

    static class GroupViewHoldder {
        TextView name;
        TextView number;

    }
}
