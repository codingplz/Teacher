package com.example.mrwen.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Lesson;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.interfaces.InterfaceLesson;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonInfoActivity extends AppCompatActivity {


    @Bind(R.id.tv_lesson_info_name)
    TextView tv_lesson_info_name;
    @Bind(R.id.tv_lesson_info_knowledgePoint)
    TextView tv_lesson_info_knowledgePoint;
    @Bind(R.id.tv_lesson_info_description)
    TextView tv_lesson_info_description;

    String lessonName;
    String knowledgePoint;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_infomation);
        ButterKnife.bind(this);
        retrofitGetSingleLessonInfo();

        final MyDialog myDialog=new MyDialog();

        //修改课时名称
        tv_lesson_info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(LessonInfoActivity.this,"修改课时名称",tv_lesson_info_name.getText().toString(),tv_lesson_info_name);
            }
        });

        //修改课时知识点
        tv_lesson_info_knowledgePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(LessonInfoActivity.this,"修改课时知识点",tv_lesson_info_knowledgePoint.getText().toString(),tv_lesson_info_knowledgePoint);
            }
        });

        //修改课时介绍
        tv_lesson_info_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(LessonInfoActivity.this,"修改课时介绍",tv_lesson_info_description.getText().toString(),tv_lesson_info_description);
            }
        });




    }

    //retrofit获得章节信息
    private void retrofitGetSingleLessonInfo(){
        Retrofit retrofitGetSingleLessonInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();

        final InterfaceLesson getSingleLessonInfo=retrofitGetSingleLessonInfo.create(InterfaceLesson.class);
        final Call<Lesson> call=getSingleLessonInfo.getSingleLessonInfo(Integer.parseInt(StaticInfo.currentLessonId));
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {

                Lesson lesson=response.body();
                tv_lesson_info_name.setText(lesson.getLessonName());

                tv_lesson_info_knowledgePoint.setText(lesson.getKnowledgePoint());
                tv_lesson_info_description.setText(lesson.getDescription());

            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                t.printStackTrace();
                alertDialog.showAlertDialgo(LessonInfoActivity.this,t.toString());
            }
        });
    }

    //actionBar显示创建课程的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_save, menu);
        return true;
    }
    //保存个人信息menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_save_item) {
            retofitLessonInfoRevise();
        }
        return super.onOptionsItemSelected(item);
    }
    private Map<String,String> getInfo(){
        lessonName=tv_lesson_info_name.getText().toString();
        knowledgePoint=tv_lesson_info_knowledgePoint.getText().toString();
        description=tv_lesson_info_description.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("lessonName",lessonName);
        map.put("knowledgePoint",knowledgePoint);
        map.put("description",description);
        map.put("lessonId",String.valueOf(StaticInfo.currentLessonId));
        return map;
    }

    private void retofitLessonInfoRevise(){
        Map<String,String> map=getInfo();
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceLesson lesssonInfoRevise=retrofit.create(InterfaceLesson.class);
        final Call<UniversalResult> call =lesssonInfoRevise.lessonInfoRevise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                final UniversalResult loginInResult=response.body();

                int resultCode=loginInResult.getResultCode();

                if(resultCode==1){

                    new AlertDialog.Builder(LessonInfoActivity.this).setTitle("提示")
                            .setMessage("保存课时信息成功")
                            .setPositiveButton("确认",null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(LessonInfoActivity.this,t.toString());
            }
        });
    }
}
