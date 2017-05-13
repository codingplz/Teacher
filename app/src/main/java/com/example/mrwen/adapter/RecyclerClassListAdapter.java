package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.AdminClass;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2017/2/18.
 */

public class RecyclerClassListAdapter extends BaseRecyclerViewAdapter<AdminClass,RecyclerClassListAdapter.MyClassesViewHolder> {
    public RecyclerClassListAdapter(ArrayList<AdminClass> data) {
        super(data);
    }
    //private OnUserInfoClickListener mOnUserInfoClickListener;



    @Override
    public MyClassesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_class_list, parent, false);
        return new MyClassesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyClassesViewHolder holder, int position) {
        final AdminClass adminClass = data.get(position);
//        Glide.with(UiUtils.getContext())
//                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + user.getImageURL())
//                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
//                .transform(new GlideCircleTransform(UiUtils.getContext()))
//                .into(holder.ivImage);
        holder.tv_class_region.setText(adminClass.getRegion());
        holder.tv_class_name.setText(adminClass.getSchool()+adminClass.getGrade());
        holder.itemView.setTag(adminClass);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (AdminClass) v.getTag());
            }
        });

    }

    class MyClassesViewHolder extends RecyclerView.ViewHolder{
        TextView tv_class_region;
        TextView tv_class_name;
        ImageView iv_class_cover;

        public MyClassesViewHolder(View itemView) {
            super(itemView);
            tv_class_region = ButterKnife.findById(itemView, R.id.tv_class_region);
            tv_class_name = ButterKnife.findById(itemView, R.id.tv_class_name);
            iv_class_cover=ButterKnife.findById(itemView,R.id.iv_class_cover);
        }
    }
}