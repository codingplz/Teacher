package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerGroupsAdapter;
import com.example.mrwen.bean.ChatGroup;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupsActivity extends AppCompatActivity {

    @Bind(R.id.recycler_general)
    RecyclerView mRecyclerGeneral;
    private RecyclerGroupsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerGeneral.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerGroupsAdapter(new ArrayList<ChatGroup>());
        mRecyclerGeneral.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ChatGroup>() {
            @Override
            public void onItemClick(View v, ChatGroup data) {
                Intent intent = new Intent(Intent.ACTION_VIEW, StaticInfo.getGroupChatUri("g"+data.getId(),data.getName()));
                startActivity(intent);
            }
        });
    }
    private void initData() {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getGroups=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<ChatGroup>> call=getGroups.getGroups(StaticInfo.uid);
        call.enqueue(new Callback<ArrayList<ChatGroup>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatGroup>> call, Response<ArrayList<ChatGroup>> response) {
                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<ChatGroup>> call, Throwable t) {
                alertDialog.showAlertDialgo(GroupsActivity.this,t.toString());
            }
        });
    }

}
