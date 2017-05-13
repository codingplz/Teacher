package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerClassListAdapter;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.interfaces.InterfaceClass;
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

public class ClassListActivity extends AppCompatActivity {
    @Bind(R.id.universal_recycle_view)
    RecyclerView recyclerClass;

    private RecyclerClassListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycle_list);
        ButterKnife.bind(this);
        initView();;
        initData();
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<AdminClass>() {
            @Override
            public void onItemClick(View v, AdminClass data) {
                StaticInfo.currentClassId=data.getId();
                Intent intent=new Intent(ClassListActivity.this,StudentListActivity.class);
                intent.putExtra("adminClass",data);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mAdapter = new RecyclerClassListAdapter(new ArrayList<AdminClass>());
        recyclerClass.setLayoutManager(new LinearLayoutManager(this));
        recyclerClass.setAdapter(mAdapter);
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getClass=retrofit.create(InterfaceClass.class);
        final Call<ArrayList<AdminClass>> call=getClass.getClass(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<ArrayList<AdminClass>>() {
            @Override
            public void onResponse(Call<ArrayList<AdminClass>> call, Response<ArrayList<AdminClass>> response) {
                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<AdminClass>> call, Throwable t) {
                alertDialog.showAlertDialgo(ClassListActivity.this,t.toString());
            }
        });
    }
}
