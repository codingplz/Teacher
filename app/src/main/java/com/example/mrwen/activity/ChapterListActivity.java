package com.example.mrwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.ActivityManager;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.adapter.RecyclerChapterListAdapter;
import com.example.mrwen.adapter.RecyclerLessonListAdapter;
import com.example.mrwen.bean.Chapter;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.interfaces.InterfaceLesson;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.otherclass.ChapterPartInfo;
import com.example.mrwen.otherclass.LessonPartInfo;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnChapterListClickListener;
import com.example.mrwen.view.OnChapterListMenuListener;
import com.example.mrwen.view.OnGetIdListener;
import com.example.mrwen.view.OnLessonDeleteListener;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;
import com.example.mrwen.view.SyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChapterListActivity extends AppCompatActivity {


    private RecyclerView mRecycleView;
    private RecyclerChapterListAdapter mRecyclerChapterListAdapter;
    ArrayList<ChapterPartInfo> chapterPartInfoList;
    private RecyclerLessonListAdapter plla;
    private List<List<LessonPartInfo>> lessonList = new ArrayList<>();
    RecyclerView rv;
    View subView;
    int chapterNumber;
    MyDialog alertDialog = new MyDialog();
    private String[] chapterIndex = new String[]{"第一单元", "第二单元", "第三单元", "第四单元", "第五单元", "第六单元", "第七单元", "第八单元", "第九单元", "第十单元"};
    private String[] lessonIndex = new String[]{"第一课时", "第二课时", "第三课时", "第四课时", "第五课时", "第六课时", "第七课时", "第八课时", "第九课时", "第十课时"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_chapter_list);
        mRecyclerChapterListAdapter = new RecyclerChapterListAdapter();
        mRecycleView.setLayoutManager(new SyLinearLayoutManager(ChapterListActivity.this));
        mRecycleView.setAdapter(mRecyclerChapterListAdapter);
        retrofitGetChapterInfo();
        retrofitGetLessonInfo();
        mRecyclerChapterListAdapter.setOnChapterListClickListener(new OnChapterListClickListener() {
            @Override
            public void onChapterListClick(View v, ChapterPartInfo data, final int position) {
                StaticInfo.currentChapterId = String.valueOf(data.getId());
                if (lessonList.size() != 0) {

                    LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.layout_chapter_list);
                    subView = LayoutInflater.from(v.getContext()).inflate(R.layout.item_chapter_list_add, (ViewGroup) v, false);
                    rv = (RecyclerView) subView.findViewById(R.id.recycler_lesson_list);
                    rv.setLayoutManager(new SyLinearLayoutManager(ChapterListActivity.this));
                    plla = new RecyclerLessonListAdapter(ChapterListActivity.this, lessonList.get(position));
                    rv.setAdapter(plla);
                    plla.setOnClickListener(new OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View v, Object data) {
                            LessonPartInfo lesson = (LessonPartInfo) data;
                            StaticInfo.currentLessonId = String.valueOf(lesson.getId());
                            Intent intentLesson = new Intent(ChapterListActivity.this, LessonCheckActivity.class);
                            startActivity(intentLesson);
                        }
                    });
                    plla.setOnLessonDeleteListener(new OnLessonDeleteListener() {
                        @Override
                        public void onLessonDeleteClick(int id, String name) {
                            showSingleLineInputDialog(id, name);
                        }
                    });
                    int index;
                    // 利用cell控件的Tag值来标记cell是否被点击过,因为已经将重用机制取消，cell退出当前界面时就会被销毁，Tag值也就不存在了。
                    // 如果不取消重用，那么将会出现未曾点击就已经添加子视图的效果，再点击的时候会继续添加而不是收回。
                    if (v.findViewById(R.id.layout_chapter_list).getTag() == null) {
                        index = 1;
                    } else {
                        index = (int) v.findViewById(R.id.layout_chapter_list).getTag();
                    }

                    // close状态: 添加视图
                    if (index == 1) {
                        linearLayout.addView(subView);
                        subView.setTag(1000);
                        v.findViewById(R.id.layout_chapter_list).setTag(2);
                    } else {
                        // open状态： 移除视图
                        linearLayout.removeView(v.findViewWithTag(1000));
                        v.findViewById(R.id.layout_chapter_list).setTag(1);
                    }
                }
            }
        });

        mRecyclerChapterListAdapter.setOnClickListener(new OnChapterListMenuListener() {
            @Override
            public void onChapterListMenuListener(int id, View v, int isSigned,ChapterPartInfo cpi) {
                StaticInfo.currentChapterId = String.valueOf(id);
                showPopup(v, isSigned,cpi);
            }
        });
    }

    public void showPopup(View v, final int isSigned,final ChapterPartInfo cpi) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_chapter_list, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                // TODO Auto-generated method stub
                switch (arg0.getItemId()) {
                    case R.id.menu_item_chapter_name:
                        Intent intentChapterInformation = new Intent(ChapterListActivity.this, ChapterInfoActivity.class);
                        startActivity(intentChapterInformation);
                        return true;
                    case R.id.menu_item_chapter_add_lesson:
                        if(Integer.parseInt(cpi.getLessonNumber())<=10) {
                            Intent intentLessonEdit = new Intent(ChapterListActivity.this, LessonEditAcitvity.class);
                            intentLessonEdit.putExtra("isSigned", isSigned);
                            ActivityManager.addDestoryActivity(ChapterListActivity.this, "ChapterListActivity");
                            startActivity(intentLessonEdit);
                        }else{
                            alertDialog.showAlertDialgo(ChapterListActivity.this, "该课程单元数目过多");
                        }
                        return true;
                    case R.id.menu_item_chapter_delete:
                        if(Integer.parseInt(cpi.getLessonNumber())!=0){
                            new MyDialog().showAlertDialgo(ChapterListActivity.this,"该单元下包含课程，需先删除该单元课程");
                        }else{
                            showDeleteChapterInputDialog(cpi.getId(),cpi.getName());
                        }

                    default:
                        return false;
                }
            }
        });
    }

    //actionBar显示创建单元的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_chapter, menu);
        return true;
    }

    //创建单元menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.create_new_chapter_item) {
            if (chapterNumber <= 10) {
                Intent intentCourseEdit = new Intent(ChapterListActivity.this, ChapterEditAcitvity.class);
                startActivity(intentCourseEdit);
            } else {
                alertDialog.showAlertDialgo(ChapterListActivity.this, "该课程单元数目过多");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //retrofit获得章节部分信息
    private void retrofitGetChapterInfo() {
        Retrofit retrofitGetChapterInfo = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog = new MyDialog();
        final InterfaceChapter getChapterInfo = retrofitGetChapterInfo.create(InterfaceChapter.class);
        final Call<List<Chapter>> call = getChapterInfo.getChapterInfo(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {

                List<Chapter> chapterList = response.body();
                chapterNumber = chapterList.size();
                chapterPartInfoList = new ArrayList<ChapterPartInfo>();
                for (int i = 0; i < chapterList.size(); i++) {
                    ChapterPartInfo chapterPartInfo = new ChapterPartInfo();
                    chapterPartInfo.setId(chapterList.get(i).getId());
                    chapterPartInfo.setIndex(chapterIndex[i]);
                    chapterPartInfo.setName(chapterList.get(i).getChapterName());
                    chapterPartInfo.setIsSigned(chapterList.get(i).getIsSigned());
                    chapterPartInfo.setLessonNumber(chapterList.get(i).getLessonNumber());
                    chapterPartInfoList.add(chapterPartInfo);
                }
                mRecyclerChapterListAdapter.setData(chapterPartInfoList);
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterListActivity.this, t.toString());
            }
        });
    }


    //retrofit获得lesson部分信息
    private void retrofitGetLessonInfo() {
        Retrofit retrofitGetLessonInfo = new Retrofit.Builder()
                .baseUrl(UiUtils.getContext().getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceLesson getAllLessonInfo = retrofitGetLessonInfo.create(InterfaceLesson.class);
        final Call<List<List<LessonPartInfo>>> call = getAllLessonInfo.getAllLessonInfo(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<List<List<LessonPartInfo>>>() {
            @Override
            public void onResponse(Call<List<List<LessonPartInfo>>> call, Response<List<List<LessonPartInfo>>> response) {
                if (response.body() != null) {
                    lessonList = response.body();
                    for (List<LessonPartInfo> ll : lessonList) {
                        for (int i = 0; i < ll.size(); i++) {
                            ll.get(i).setIndex(lessonIndex[i]);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<List<LessonPartInfo>>> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterListActivity.this, t.toString());
            }
        });
    }

    private void retrofitDeleteLesson(int id, final String name) {
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceLesson deleteLesson = retrofit.create(InterfaceLesson.class);
        final Call<UniversalResult> call = deleteLesson.deleteLesson(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChapterListActivity.this);
                    builder.setTitle("删除课时《" + name + "》成功").setIcon(
                            R.drawable.ic_arrow_check).setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ChapterListActivity.this, ChapterListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    builder.show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterListActivity.this, t.toString());
            }
        });
    }

    private void retrofitDeleteChapter(int id, final String name) {
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceChapter deleteChapter = retrofit.create(InterfaceChapter.class);
        final Call<UniversalResult> call = deleteChapter.deleteChapter(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChapterListActivity.this);
                    builder.setTitle("删除单元《" + name + "》成功").setIcon(
                            R.drawable.ic_arrow_check).setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ChapterListActivity.this, ChapterListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    builder.show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(ChapterListActivity.this, t.toString());
            }
        });
    }

    //弹出单行输入框
    public void showSingleLineInputDialog(final int id, final String lessonName) {
        final EditText inputServer = new EditText(this);
        inputServer.setMaxLines(1);
        inputServer.setMinLines(1);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入您想要删除课时的名称").setIcon(
                R.drawable.ic_arrow_check).setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        if (name.equals(lessonName)) {
                            retrofitDeleteLesson(id, lessonName);
                        } else {
                            new MyDialog().showAlertDialgo(ChapterListActivity.this, "请输入正确的课时名");
                        }
                    }
                });
        builder.show();
    }

    //弹出课程删除
    public void showDeleteChapterInputDialog(final int id, final String chapterName) {
        final EditText inputServer = new EditText(this);
        inputServer.setMaxLines(1);
        inputServer.setMinLines(1);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入您想要删除单元的名称").setIcon(
                R.drawable.ic_arrow_check).setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        if (name.equals(chapterName)) {
                            retrofitDeleteChapter(id, chapterName);
                        } else {
                            new MyDialog().showAlertDialgo(ChapterListActivity.this, "请输入正确的单元名");
                        }
                    }
                });
        builder.show();
    }
}
