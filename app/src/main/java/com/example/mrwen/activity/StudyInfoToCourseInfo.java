package com.example.mrwen.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Course;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.staticClass.StaticInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyInfoToCourseInfo extends AppCompatActivity {

    @Bind(R.id.tv_course_check_courseName)
    TextView tv_course_check_courseName;
    @Bind(R.id.iv_course_check_cover)
    ImageView iv_course_check_cover;
    @Bind(R.id.tv_course_check_subject)
    TextView tv_course_check_subject;
    @Bind(R.id.tv_course_check_grade)
    TextView tv_course_check_grade;
    @Bind(R.id.tv_course_check_description)
    TextView tv_course_check_description;
    @Bind(R.id.tv_course_check_chapterNumber)
    TextView tv_course_check_chapterNumber;
    @Bind(R.id.tv_course_check_semester)
    TextView tv_course_check_semester;
    @Bind(R.id.tv_course_check_major)
    TextView tv_course_check_major;
    @Bind(R.id.tv_course_check_teacher)
    TextView tv_course_check_teacher;
    @Bind(R.id.layout_course_check_teacher)
    LinearLayout layout_course_check_teacher;

    int teacherId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_info_to_course_info);
        ButterKnife.bind(this);
        int id=getIntent().getIntExtra("courseId",0);
        retrofitGetSingleCourseInfo(id);
        layout_course_check_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacherId!=0){
                    String uid="t"+teacherId;
                    Intent i=new Intent(StudyInfoToCourseInfo.this,PersonInfoActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    //retrofit获得课程信息
    private void retrofitGetSingleCourseInfo(int id){
        Retrofit retrofitGetSingleCourseInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();
        final InterfaceCourse getSingleCourseInfo=retrofitGetSingleCourseInfo.create(InterfaceCourse.class);
        final Call<Course> call=getSingleCourseInfo.getSingleCourseInfo(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Course course=response.body();
                if(course==null)
                StaticInfo.currentCourseId=String.valueOf(course.getId());
                tv_course_check_courseName.setText(course.getName());
                tv_course_check_subject.setText(course.getSubject());
                tv_course_check_grade.setText(course.getGrade());
                tv_course_check_description.setText(course.getDescription());
                tv_course_check_chapterNumber.setText(course.getChapterNumber());
                if(course.getSemester()!=null)
                    tv_course_check_semester.setText(course.getSemester());
                if(course.getMajor()!=null)
                    tv_course_check_major.setText(course.getMajor().getTitle());
                if(course.getCoverURL()!=null)
                    Glide.with(StudyInfoToCourseInfo.this).load(getResources().getString(R.string.baseURL) + course.getCoverURL()).into(iv_course_check_cover);
                tv_course_check_teacher.setText(course.getTeacher().getRealname());
                teacherId=course.getTeacher().getId();
                Log.i("teacherIdddd",teacherId+"");
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                alertDialog.showAlertDialgo(StudyInfoToCourseInfo.this,t.toString());
            }
        });
    }
}
