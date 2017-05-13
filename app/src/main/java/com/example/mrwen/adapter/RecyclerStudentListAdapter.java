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
import com.example.mrwen.bean.Student;
import com.example.mrwen.view.OnChatListener;
import com.example.mrwen.view.OnStudyInfoClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by fate on 2017/2/18.
 */

public class RecyclerStudentListAdapter extends BaseRecyclerViewAdapter<Student,RecyclerStudentListAdapter.MyStudentsViewHolder> {
    public RecyclerStudentListAdapter(ArrayList<Student> data) {
        super(data);
    }
    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnStudyInfoClickListener mOnStudyInfoClickListener;
    private OnChatListener mOnChatListener;

    public void setOnStudyInfoListener(OnStudyInfoClickListener onStudyInfoClickListener) {
        mOnStudyInfoClickListener = onStudyInfoClickListener;
    }
    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }
    public void setmOnChatListener(OnChatListener onChatListener){
        mOnChatListener=onChatListener;
    }

    @Override
    public MyStudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_student_list, parent, false);
        return new MyStudentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyStudentsViewHolder holder, int position) {
        final ArrayList<Integer> idArray=new ArrayList<>();
        final ArrayList<String> nameArray=new ArrayList<>();
        for(Student s:data){
            idArray.add(s.getId());
            nameArray.add(s.getRealname());
        }
        final Student student = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL) + student.getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .into(holder.iv_student_list_image);
        holder.tv_student_list_name.setText(student.getRealname());
        holder.tv_student_list_signature.setText(student.getSignature());
        holder.itemView.setTag(student);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (Student) v.getTag());
            }
        });
        holder.layout_study_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStudyInfoClickListener !=null)
                    mOnStudyInfoClickListener.onStudyInfoClickListener(student.getId(),student.getRealname(),idArray,nameArray);
            }
        });
        holder.iv_student_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnUserInfoClickListener!=null)
                    mOnUserInfoClickListener.onUserInfoClickListener("s"+student.getId());
            }
        });
        holder.layout_student_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnChatListener!=null)
                    mOnChatListener.onChatListener("s"+student.getId(),student.getRealname());
            }
        });

    }

    class MyStudentsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_student_list_name;
        TextView tv_student_list_signature;
        ImageView iv_student_list_image;
        LinearLayout layout_study_info;
        LinearLayout layout_student_list;

        public MyStudentsViewHolder(View itemView) {
            super(itemView);
            tv_student_list_name = ButterKnife.findById(itemView, R.id.tv_student_list_name);
            tv_student_list_signature = ButterKnife.findById(itemView, R.id.tv_student_list_signature);
            iv_student_list_image=ButterKnife.findById(itemView,R.id.iv_student_list_image);
            layout_study_info=ButterKnife.findById(itemView,R.id.layout_study_info);
            layout_student_list=ButterKnife.findById(itemView,R.id.layout_student_list);
        }
    }
}