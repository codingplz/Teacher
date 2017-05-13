package com.example.mrwen.adapter;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.view.OnDeleteClickListener;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by mrwen on 2017/3/23.
 */

public class RecyclerExerciseUploadAdapter extends BaseRecyclerViewAdapter<Exercise,RecyclerExerciseUploadAdapter.MyTestsViewHolder> {

    private OnRecyclerViewItemClickListener exerciseChecklistener;
    ArrayList<Integer> exercises = new ArrayList<>();
    ArrayList<Integer> hasChecked = new ArrayList<>();

    public RecyclerExerciseUploadAdapter(ArrayList<Exercise> data,ArrayList<Integer> exercises,ArrayList<Integer> hasChecked) {
        super(data);
        this.exercises=exercises;
        this.hasChecked=hasChecked;
    }


    @Override
    public MyTestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_exercise_upload, parent, false);
        return new MyTestsViewHolder(view);
    }

    public void setExerciseCheckListener(OnRecyclerViewItemClickListener<Exercise> listener){
        this.exerciseChecklistener = listener;
    }


    @Override
    public void onBindViewHolder(MyTestsViewHolder holder, int position) {
        final Exercise cq = data.get(position);
        holder.tv_test_title.setText(cq.getTitle());
        holder.tv_test_optionA.setText("A."+cq.getOptionA());
        holder.tv_test_optionB.setText("B."+cq.getOptionB());
        holder.tv_test_optionC.setText("C."+cq.getOptionC());
        holder.tv_test_optionD.setText("D."+cq.getOptionD());
        holder.tv_test_number.setText("作答"+cq.getNumber()+"次");
        holder.tv_test_difficulty.setText("难度"+cq.getDifficulty());
        holder.tv_test_accuracy.setText("正确率"+cq.getAccuracy()+"%");

        holder.layout_test_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, cq);
            }
        });

        holder.iv_test_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exerciseChecklistener!=null)
                    exerciseChecklistener.onItemClick(v, cq);
            }
        });

        for(int i:exercises){
            if(cq.getId()==i){
                holder.iv_test_check.setImageResource(R.drawable.ic_check_circle_red_500_24dp);
            }
        }
        if(hasChecked!=null) {
            for (int i : hasChecked) {
                if (cq.getId() == i) {
                    holder.iv_test_check.setImageResource(R.drawable.ic_check_circle_red_500_24dp);
                    holder.iv_test_check.setEnabled(false);
                    holder.tv_test_title.setTextColor(Color.parseColor("#999999"));
                    holder.tv_test_optionA.setTextColor(Color.parseColor("#999999"));
                    holder.tv_test_optionB.setTextColor(Color.parseColor("#999999"));
                    holder.tv_test_optionC.setTextColor(Color.parseColor("#999999"));
                    holder.tv_test_optionD.setTextColor(Color.parseColor("#999999"));
                    holder.layout_test_check.setEnabled(false);
                }
            }
        }
    }
//    public void remove(int id){
//        for (Iterator iter = data.iterator(); iter.hasNext();) {
//            Exercise cq=(Exercise) iter.next();
//            if (cq.getId()==id)
//                iter.remove();
//        }
//        UiUtils.runInMainThread(new Runnable() {
//            @Override
//            public void run() {
//                notifyDataSetChanged();
//            }
//        });
//    }

    class MyTestsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_test_title;
        TextView tv_test_optionA;
        TextView tv_test_optionB;
        TextView tv_test_optionC;
        TextView tv_test_optionD;
        TextView tv_test_number;
        ImageView iv_test_check;
        TextView tv_test_difficulty;
        TextView tv_test_accuracy;
        LinearLayout layout_test_check;

        public MyTestsViewHolder(View itemView) {
            super(itemView);
            tv_test_title = ButterKnife.findById(itemView, R.id.tv_test_title);
            tv_test_optionA = ButterKnife.findById(itemView, R.id.tv_test_optionA);
            tv_test_optionB = ButterKnife.findById(itemView, R.id.tv_test_optionB);
            tv_test_optionC = ButterKnife.findById(itemView, R.id.tv_test_optionC);
            tv_test_optionD = ButterKnife.findById(itemView, R.id.tv_test_optionD);
            tv_test_number = ButterKnife.findById(itemView, R.id.tv_test_number);
            iv_test_check = ButterKnife.findById(itemView, R.id.iv_test_check);
            layout_test_check = ButterKnife.findById(itemView, R.id.layout_test_check_in_activity);
            tv_test_difficulty = ButterKnife.findById(itemView, R.id.tv_test_difficulty);
            tv_test_accuracy = ButterKnife.findById(itemView, R.id.tv_test_accuracy);
        }
    }
}