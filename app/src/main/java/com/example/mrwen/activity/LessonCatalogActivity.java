package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.LessonAdapter;
import com.example.mrwen.bean.Lesson;
import com.example.mrwen.interfaces.InterfaceLesson;
import com.example.mrwen.otherclass.LessonPartInfo;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonCatalogActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private LessonAdapter lessonAdapter;
    private int isSigned;
    private String[] lessonIndex =new String[]{"第一课时","第二课时","第三课时","第四课时","第五课时","第六课时","第七课时","第八课时","第九课时"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_check);

        Intent intent=getIntent();
        isSigned=intent.getIntExtra("isSigned",0);

        expandableListView=(ExpandableListView)findViewById(R.id.el_lessonCheck);
        retrofitGetLessonInfo();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {

                StaticInfo.currentLessonId=String.valueOf(lessonAdapter.getGroup(groupPosition).getId());
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {

                switch (childPosition) {
                    //课时信息
                    case 0:
                        Intent intentLessonInformation = new Intent(LessonCatalogActivity.this, LessonInfoActivity.class);
                        startActivity(intentLessonInformation);
                        break;
                    //视频查看
                    case 1:
                        Intent intentVideo = new Intent(LessonCatalogActivity.this, VideoCheck.class);
                        startActivity(intentVideo);
                        break;
//                    //查看课时
//                    case 2:


                }
                return true;
            }
        });

    }
    //actionBar显示创建课时的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_lesson, menu);
        return true;
    }

    //创建课程menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_new_lesson_item) {
            Intent intentLessonEdit=new Intent(LessonCatalogActivity.this,LessonEditAcitvity.class);
            intentLessonEdit.putExtra("isSigned",isSigned);
            startActivity(intentLessonEdit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //retrofit获得lesson部分信息
    private void retrofitGetLessonInfo(){
        Retrofit retrofitGetLessonInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();
        final InterfaceLesson getLessonInfo=retrofitGetLessonInfo.create(InterfaceLesson.class);
        final Call<List<Lesson>> call=getLessonInfo.getLessonInfo(Integer.parseInt(StaticInfo.currentChapterId));
        call.enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {

                List<Lesson> lessonList=response.body();

                //根据id进行排序
                Collections.sort(lessonList, new Comparator<Lesson>(){

                    /*
                     * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                     * 返回负数表示：o1 小于o2，
                     * 返回0 表示：o1和o2相等，
                     * 返回正数表示：o1大于o2。
                     *
                     */public int compare(Lesson o1, Lesson o2) {

                        //按照学生的年龄进行升序排列
                        if(o1.getId() > o2.getId()){
                            return 1;
                        }
                        if(o1.getId() == o2.getId()){
                            return 0;
                        }
                        return -1;
                    }
                });
                List<LessonPartInfo> lessonPartInfoList=new ArrayList<LessonPartInfo>();
                for(int i=0;i<lessonList.size();i++){
                    LessonPartInfo chapterPartInfo=new LessonPartInfo();
                    chapterPartInfo.setId(lessonList.get(i).getId());
                    chapterPartInfo.setIndex(lessonIndex[i]);
                    chapterPartInfo.setName(lessonList.get(i).getLessonName());
                    chapterPartInfo.setIsUpload(lessonList.get(i).getIsUpload());
                    lessonPartInfoList.add(chapterPartInfo);
                }
                lessonAdapter =new LessonAdapter(LessonCatalogActivity.this,lessonPartInfoList);
                expandableListView.setAdapter(lessonAdapter);


            }


            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                alertDialog.showAlertDialgo(LessonCatalogActivity.this,t.toString());
            }
        });
    }
}
