package com.example.mrwen.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.activity.ChapterListActivity;
import com.example.mrwen.activity.CourseCheckActivity;
import com.example.mrwen.adapter.CourseItemAdapter;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.otherclass.CoursePartInfo;
import com.example.mrwen.activity.R;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnCourseDeleteListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mrwen on 2016/10/29.
 */

public class CourseFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<CoursePartInfo> coursePartInfoList;
    private CourseItemAdapter commonAdapter;
    private List<Course> courseList;
    private MyDialog alertDialog = new MyDialog();


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_course_main, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.courseRecyclerview);
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getCourseInformation();

    }

    private void getCourseInformation() {

        Retrofit retrofitGetCourseInfo = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceCourse getCourseInfo = retrofitGetCourseInfo.create(InterfaceCourse.class);
        final Call<List<Course>> call = getCourseInfo.getCourseInfo(StaticInfo.id);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                courseList = response.body();
                coursePartInfoList = new ArrayList<CoursePartInfo>();
                if (courseList.size() != 0) {
                    for (int i = 0; i < courseList.size(); i++) {
                        Course course = courseList.get(i);
                        CoursePartInfo coursePartInfo = new CoursePartInfo(course.getName(), course.getFocusNumber(), "已上传 " + course.getChapterNumber() + " 个单元", course.getCoverURL(), course.getChapterNumber());
                        coursePartInfo.setCourseId(String.valueOf(course.getId()));
                        coursePartInfoList.add(coursePartInfo);
                    }

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(commonAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    //创建适配器
                    commonAdapter = new CourseItemAdapter(coursePartInfoList);
                    //设置设配器
                    mRecyclerView.setAdapter(commonAdapter);
                    commonAdapter.setOnItemClickListener(new CourseItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, CoursePartInfo data) {
                            StaticInfo.currentCourseId = data.getCourseId();
                            Intent intentCourseCheck = new Intent(getActivity(), CourseCheckActivity.class);
                            startActivityForResult(intentCourseCheck, 1);
                        }
                    });
                    commonAdapter.setOnCourseDeleteListener(new OnCourseDeleteListener() {
                        @Override
                        public void onCourseDeleteClick(int id, int chapterNumber, String courseName) {
                            if (chapterNumber != 0) {
                                new MyDialog().showAlertDialgo(getContext(), "该课程下包含课程，需先删除该课程单元");
                            } else {
                                showDeleteCourseInputDialog(id, courseName);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                alertDialog.showAlertDialgo(getContext(), "获取课程失败" + t.toString());
            }
        });
    }

    public void showDeleteCourseInputDialog(final int id, final String courseName) {
        final EditText inputServer = new EditText(getContext());
        inputServer.setMaxLines(1);
        inputServer.setMinLines(1);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("请输入您想要删除课程的名称").setIcon(
                R.drawable.ic_arrow_check).setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        if (name.equals(courseName)) {
                            retrofitDeleteCourse(id, courseName);
                        } else {
                            new MyDialog().showAlertDialgo(getContext(), "请输入正确的课程名");
                        }
                    }
                });
        builder.show();
    }

    private void retrofitDeleteCourse(int id, final String name) {
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse deleteCourse = retrofit.create(InterfaceCourse.class);
        final Call<UniversalResult> call = deleteCourse.deleteCourse(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("删除课程《" + name + "》成功").setIcon(
                            R.drawable.ic_arrow_check).setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getCourseInformation();
                                }
                            });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(getContext(), t.toString());
            }
        });
    }

}