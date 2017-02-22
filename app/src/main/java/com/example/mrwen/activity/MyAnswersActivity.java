package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerMyAnswersAdapter;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.Result;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnDeleteClickListener;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAnswersActivity extends AppCompatActivity {

    @Bind(R.id.recycler_general)
    RecyclerView recyclerAnswers;
    private RecyclerMyAnswersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_answers);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        mAdapter = new RecyclerMyAnswersAdapter(new ArrayList<Answer>());
        recyclerAnswers.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnswers.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Answer>() {
            @Override
            public void onItemClick(View v, Answer data) {
                Intent intent = new Intent(MyAnswersActivity.this,QuestionActivity.class);
                intent.putExtra("issue",data.getIssue());
                startActivity(intent);
            }
        });
        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int id) {
                final int thisId=id;
                new android.support.v7.app.AlertDialog.Builder(MyAnswersActivity.this).setTitle("提示")
                        .setMessage("您确认要删除该回答么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAnswer(thisId);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });
        mAdapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent = new Intent(MyAnswersActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
    }

    private void deleteAnswer(final int id) {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher deleteAnswer=retrofit.create(InterfaceTeacher.class);
        final Call<Result> call=deleteAnswer.deleteAnswer(id);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                mAdapter.remove(id);
                Toast.makeText(MyAnswersActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MyAnswersActivity.this, "删除失败"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher loadMyAnswer=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Answer>> call=loadMyAnswer.loadMyAnswer(StaticInfo.uid);
        call.enqueue(new Callback<ArrayList<Answer>>() {
            @Override
            public void onResponse(Call<ArrayList<Answer>> call, Response<ArrayList<Answer>> response) {
                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Answer>> call, Throwable t) {
                alertDialog.showAlertDialgo(MyAnswersActivity.this,t.toString());
            }
        });
    }


}
