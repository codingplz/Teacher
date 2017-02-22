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
import com.example.mrwen.bean.Chapter;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.interfaces.InterfaceCourse;
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

public class ChapterInfoActivity extends AppCompatActivity {

    @Bind(R.id.tv_chapter_info_name)
    TextView tv_chapter_info_name;
    @Bind(R.id.tv_chapter_info_lessonNumber)
    TextView tv_chapter_info_lessonNumber;
    @Bind(R.id.tv_chapter_info_knowledgePoint)
    TextView tv_chapter_info_knowledgePoint;
    @Bind(R.id.tv_chapter_info_description)
    TextView tv_chapter_description;

    String chapterName;
    String lessonNumber;
    String knowledgePoint;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_information);
        ButterKnife.bind(this);
        retrofitGetSingleChapterInfo();

        final MyDialog myDialog=new MyDialog();

        //修改章节名称
        tv_chapter_info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(ChapterInfoActivity.this,"修改章节名称",tv_chapter_info_name.getText().toString(),tv_chapter_info_name);
            }
        });


        //修改知识点
        tv_chapter_info_knowledgePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(ChapterInfoActivity.this,"修改知识点",tv_chapter_info_knowledgePoint.getText().toString(),tv_chapter_info_knowledgePoint);
            }
        });

        //修改章节介绍
        tv_chapter_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(ChapterInfoActivity.this,"修改章节介绍",tv_chapter_info_knowledgePoint.getText().toString(),tv_chapter_description);
            }
        });


    }

    //retrofit获得章节信息
    private void retrofitGetSingleChapterInfo(){
        Retrofit retrofitGetSingleChapterInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();
        final InterfaceChapter getSingleChapterInfo=retrofitGetSingleChapterInfo.create(InterfaceChapter.class);
        final Call<Chapter> call=getSingleChapterInfo.getSingleChapterInfo(Integer.parseInt(StaticInfo.currentChapterId));
        call.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {

                Chapter chapter=response.body();
                tv_chapter_info_name.setText(chapter.getChapterName());

                tv_chapter_info_knowledgePoint.setText(chapter.getKnowledgePoint());
                tv_chapter_info_lessonNumber.setText(chapter.getLessonNumber());
                tv_chapter_description.setText(chapter.getDescription());

            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterInfoActivity.this,t.toString());
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info_save_item) {
            retofitChapterInfoRevise();
        }
        return super.onOptionsItemSelected(item);
    }
    //获取信息
    private Map<String,String> getInfo(){
        chapterName=tv_chapter_info_name.getText().toString();
        lessonNumber=tv_chapter_info_lessonNumber.getText().toString();
        knowledgePoint=tv_chapter_info_knowledgePoint.getText().toString();
        description=tv_chapter_description.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("chapterName",chapterName);
        map.put("lessonNumber",lessonNumber);
        map.put("knowledgePoint",knowledgePoint);
        map.put("description",description);
        map.put("chapterId",String.valueOf(StaticInfo.currentChapterId));
        return map;
    }

    private void retofitChapterInfoRevise(){
        Map<String,String> map=getInfo();
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofitLoginIn=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceChapter chapterInfoRevise=retrofitLoginIn.create(InterfaceChapter.class);
        final Call<UniversalResult> call=chapterInfoRevise.chapterInfoRevise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                final UniversalResult loginInResult=response.body();

                int resultCode=loginInResult.getResultCode();

                if(resultCode==1){

                    new AlertDialog.Builder(ChapterInfoActivity.this).setTitle("提示")
                            .setMessage("保存单元信息成功")
                            .setPositiveButton("确认",null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterInfoActivity.this,t.toString());
            }
        });
    }



}
