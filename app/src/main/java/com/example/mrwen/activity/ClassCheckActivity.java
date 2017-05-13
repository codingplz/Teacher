package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.sax.RootElement;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassCheckActivity extends AppCompatActivity {

    @Bind(R.id.tv_class_check_origin)
    TextView tv_class_check_origin;
    @Bind(R.id.tv_class_check_school)
    TextView tv_class_check_school;
    @Bind(R.id.tv_class_check_grade)
    TextView tv_class_check_grade;
    @Bind(R.id.layout_class_check_student_list)
    RelativeLayout layout_class_check_student_list;
    @Bind(R.id.layout_class_check_college_student_list)
    RelativeLayout layout_class_check_college_student_list;
    @Bind(R.id.layout_class_check_class_statistic)
    RelativeLayout layout_class_check_class_statistic;

    String origin;
    String school;
    String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_check);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        AdminClass ac=(AdminClass) intent.getSerializableExtra("adminClass");

        tv_class_check_origin.setText(ac.getRegion().replace('_',' '));
        tv_class_check_school.setText(ac.getSchool());
        tv_class_check_grade.setText(ac.getGrade());
        //retrofitGetInfo();
        layout_class_check_student_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClassCheckActivity.this,StudentListActivity.class);
                startActivity(intent);
            }
        });

    }


    private void retrofitGetInfo(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getClassInfo=retrofit.create(InterfaceClass.class);
        final Call<AdminClass> call=getClassInfo.getClassInfo(StaticInfo.currentClassId);
        call.enqueue(new Callback<AdminClass>() {
            @Override
            public void onResponse(Call<AdminClass> call, Response<AdminClass> response) {
                origin=response.body().getRegion().replace('_',' ');
                school=response.body().getSchool();
                grade=response.body().getGrade();
                tv_class_check_origin.setText(origin);
                tv_class_check_school.setText(school);
                tv_class_check_grade.setText(grade);
            }
            @Override
            public void onFailure(Call<AdminClass> call, Throwable t) {
                alertDialog.showAlertDialgo(ClassCheckActivity.this,t.toString());
            }
        });
    }

}
