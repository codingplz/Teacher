package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.ExerciseCatagory;
import com.example.mrwen.bean.Student;
import com.example.mrwen.view.OnChatListener;
import com.example.mrwen.view.OnStudyInfoClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/4/26.
 */

public class RecyclerExerciseCatagoryAdapter extends BaseRecyclerViewAdapter<ExerciseCatagory,RecyclerExerciseCatagoryAdapter.MyECViewHolder> {
    public RecyclerExerciseCatagoryAdapter(ArrayList<ExerciseCatagory> data) {
        super(data);
    }

    @Override
    public MyECViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_exercise_catagory, parent, false);
        return new MyECViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyECViewHolder holder, int position) {
        final ExerciseCatagory ec = data.get(position);
        holder.tv_name.setText(ec.getName()+"课后练习");
        holder.tv_total.setText("作答"+ec.getTotal()+"次");
        holder.tv_number.setText("共5题");
        holder.tv_difficulty.setText("难度3.0");
        holder.tv_accuracy.setText("正确率"+ec.getAccuracy()+"%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, ec);
            }
        });

    }

    class MyECViewHolder extends RecyclerView.ViewHolder{
        TextView tv_total;
        TextView tv_name;
        TextView tv_number;
        TextView tv_difficulty;
        TextView tv_accuracy;

        public MyECViewHolder(View itemView) {
            super(itemView);
            tv_total = ButterKnife.findById(itemView, R.id.tv_total);
            tv_name = ButterKnife.findById(itemView, R.id.tv_name);
            tv_number=ButterKnife.findById(itemView,R.id.tv_number);
            tv_difficulty=ButterKnife.findById(itemView,R.id.tv_difficulty);
            tv_accuracy=ButterKnife.findById(itemView,R.id.tv_accuracy);
        }
    }
}