package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrwen.Utils.ActivityManager;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerCourseNoticeAdapter;
import com.example.mrwen.adapter.RecyclerMyAnswersAdapter;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseNoticeActivity extends AppCompatActivity {

    @Bind(R.id.recycler_course_notice)
    RecyclerView recyclerNotices;
    private RecyclerCourseNoticeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notice);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    //actionBar显示发布公告的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_notice, menu);
        return true;
    }

    //创建单元menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.create_new_notice_item) {
            Intent intent=new Intent(CourseNoticeActivity.this,WriteNoticeActivity.class);
            ActivityManager.addDestoryActivity(CourseNoticeActivity.this, "CourseNoticeActivity");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(){
        mAdapter = new RecyclerCourseNoticeAdapter(new ArrayList<Notice>());
        recyclerNotices.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotices.setAdapter(mAdapter);
//        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Notice>() {
//            @Override
//            public void onItemClick(View v, Notice data) {
//
//            }
//        });
        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId=id;
                new android.support.v7.app.AlertDialog.Builder(CourseNoticeActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该公告么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNotice(thisId);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse getNotice=retrofit.create(InterfaceCourse.class);
        final Call<ArrayList<Notice>> call=getNotice.getNotice(StaticInfo.currentCourseId);
        call.enqueue(new Callback<ArrayList<Notice>>() {
            @Override
            public void onResponse(Call<ArrayList<Notice>> call, Response<ArrayList<Notice>> response) {
                if(response.body()!=null) {
                    mAdapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Notice>> call, Throwable t) {
                alertDialog.showAlertDialgo(CourseNoticeActivity.this,t.toString());
            }
        });
    }

    private void deleteNotice(final int id) {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse deleteNotice=retrofit.create(InterfaceCourse.class);
        final Call<UniversalResult> call=deleteNotice.deleteNotice(id);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body().getResultCode()==1) {
                    mAdapter.remove(id);
                    alertDialog.showAlertDialgo(CourseNoticeActivity.this,"删除公告成功");
                }else{
                    alertDialog.showAlertDialgo(CourseNoticeActivity.this,"删除公告失败");
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(CourseNoticeActivity.this,t.toString());
            }
        });
    }
}
