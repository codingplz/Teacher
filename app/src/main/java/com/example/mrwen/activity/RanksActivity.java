package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerRanksAdapter;
import com.example.mrwen.bean.CourseResult;
import com.example.mrwen.bean.Rank;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RanksActivity extends AppCompatActivity {

    @Bind(R.id.re_ranks)
    RecyclerView recyclerRanks;
    private RecyclerRanksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mAdapter = new RecyclerRanksAdapter(new ArrayList<Rank>());
        recyclerRanks.setLayoutManager(new LinearLayoutManager(this));
        recyclerRanks.setAdapter(mAdapter);
    }
    private void initData(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceCourse loadRanks=retrofit.create(InterfaceCourse.class);

        final Call<ArrayList<Rank>> call=loadRanks.loadRanks(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<ArrayList<Rank>>() {
            @Override
            public void onResponse(Call<ArrayList<Rank>> call, Response<ArrayList<Rank>> response) {
                if(response.body().size()!=0){
                    mAdapter.setData(response.body());
                }else {
                    alertDialog.showAlertDialgo(RanksActivity.this,"暂无评价");
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Rank>> call, Throwable t) {
                alertDialog.showAlertDialgo(RanksActivity.this,t.toString());
            }
        });
    }

}
