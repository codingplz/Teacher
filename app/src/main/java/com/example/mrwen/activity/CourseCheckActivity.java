package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/12/8.
 */

public class CourseCheckActivity extends AppCompatActivity{


    @Bind(R.id.layout_course_statistics)
    RelativeLayout layout_course_statistics;
    @Bind(R.id.layout_chapterCatalog)
    RelativeLayout layout_chapterCatalog;
    @Bind(R.id.layout_course_rank)
    RelativeLayout layout_course_rank;
    @Bind(R.id.layout_course_info)
    RelativeLayout layout_course_info;
    @Bind(R.id.layout_class_management)
    RelativeLayout layout_class_management;
    @Bind(R.id.layout_course_notice)
    RelativeLayout layout_course_notice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_information_check);
        ButterKnife.bind(this);

        //查看章节
        layout_chapterCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChapterEdit=new Intent(CourseCheckActivity.this,ChapterListActivity.class);
                startActivity(intentChapterEdit);
            }
        });

        //查看课程评价
        layout_course_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCourseRank=new Intent(CourseCheckActivity.this,RanksActivity.class);
                startActivity(intentCourseRank);
            }
        });

        //查看课程信息
        layout_course_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseCheckActivity.this,CourseInfoActivity.class);
                startActivity(intent);
            }
        });

        //班级管理
        layout_class_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseCheckActivity.this,ClassListActivity.class);
                startActivity(intent);
            }
        });

        //课程公告
        layout_course_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseCheckActivity.this,CourseNoticeActivity.class);
                startActivity(intent);
            }
        });
    }
}
