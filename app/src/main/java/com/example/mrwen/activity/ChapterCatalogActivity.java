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
import com.example.mrwen.adapter.ChapterAdapter;
import com.example.mrwen.bean.Chapter;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.otherclass.ChapterPartInfo;
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

public class ChapterCatalogActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ChapterAdapter chapterAdapter;
    List<ChapterPartInfo> chapterPartInfoList;
    private String[] chapterIndex=new String[]{"第一单元","第二单元","第三单元","第四单元","第五单元","第六单元","第七单元","第八单元","第九单元","第十单元"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_check);
        expandableListView=(ExpandableListView)findViewById(R.id.el_chapterCheck);
        retrofitGetChapterInfo();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {

                StaticInfo.currentChapterId=String.valueOf(chapterAdapter.getGroup(groupPosition).getId());

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {

                switch (childPosition) {
                    //单元信息
                    case 0:
                        Intent intentChapterInformation = new Intent(ChapterCatalogActivity.this, ChapterInfoActivity.class);
                        startActivity(intentChapterInformation);
                        break;
                    //单元测试

                    //查看课时
                    case 3:
                        Intent intentLesson = new Intent(ChapterCatalogActivity.this, LessonCatalogActivity.class);
                        intentLesson.putExtra("isSigned",chapterPartInfoList.get(groupPosition).getIsSigned());
                        startActivity(intentLesson);
                        break;
                }
                return true;
            }
        });

    }
    //actionBar显示创建课程的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_chapter, menu);
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
        if (id == R.id.create_new_chapter_item) {
            Intent intentCourseEdit=new Intent(ChapterCatalogActivity.this,ChapterEditAcitvity.class);
            startActivity(intentCourseEdit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //retrofit获得章节部分信息
    private void retrofitGetChapterInfo(){
        Retrofit retrofitGetChapterInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();
        final InterfaceChapter getChapterInfo=retrofitGetChapterInfo.create(InterfaceChapter.class);
        final Call<List<Chapter>> call=getChapterInfo.getChapterInfo(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {

                List<Chapter> chapterList=response.body();
                //根据id进行排序
                Collections.sort(chapterList, new Comparator<Chapter>(){

                    /*
                     * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                     * 返回负数表示：o1 小于o2，
                     * 返回0 表示：o1和o2相等，
                     * 返回正数表示：o1大于o2。
                     */public int compare(Chapter o1, Chapter o2) {

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
                chapterPartInfoList=new ArrayList<ChapterPartInfo>();
                for(int i=0;i<chapterList.size();i++){
                    ChapterPartInfo chapterPartInfo=new ChapterPartInfo();
                    chapterPartInfo.setId(chapterList.get(i).getId());
                    chapterPartInfo.setIndex(chapterIndex[i]);
                    chapterPartInfo.setName(chapterList.get(i).getChapterName());
                    chapterPartInfo.setIsSigned(chapterList.get(i).getIsSigned());
                    chapterPartInfoList.add(chapterPartInfo);
                }
                chapterAdapter=new ChapterAdapter(ChapterCatalogActivity.this,chapterPartInfoList);
                expandableListView.setAdapter(chapterAdapter);


            }


            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterCatalogActivity.this,t.toString());
            }
        });
    }
}
