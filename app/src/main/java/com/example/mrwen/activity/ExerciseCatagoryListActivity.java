package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerExerciseCatagoryAdapter;
import com.example.mrwen.adapter.RecyclerStudentListAdapter;
import com.example.mrwen.bean.ExerciseCatagory;
import com.example.mrwen.bean.Student;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseCatagoryListActivity extends AppCompatActivity {
    @Bind(R.id.universal_recycle_view)
    RecyclerView mRecycler;
    @Bind(R.id.bt_upload_test)
    Button upload;

    private RecyclerExerciseCatagoryAdapter mAdapter;
    ArrayList<ExerciseCatagory> list=new ArrayList<ExerciseCatagory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_catagory_list);
        ButterKnife.bind(this);
        initView();
        initData();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExerciseCatagoryListActivity.this,ExerciseUploadActivity.class);
                intent.putExtra("lessonName",getIntent().getStringExtra("lessonName"));
                intent.putExtra("type","upload");
                startActivityForResult(intent,2);
            }
        });
    }

    private void initView() {
        mAdapter = new RecyclerExerciseCatagoryAdapter(new ArrayList<ExerciseCatagory>());
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ExerciseCatagory>() {
            @Override
            public void onItemClick(View v, ExerciseCatagory data) {
                Intent intent=new Intent(ExerciseCatagoryListActivity.this,TestActivity.class);
                intent.putExtra("ecId",data.getId());
                intent.putExtra("lessonName",getIntent().getStringExtra("lessonName"));
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
        final InterfaceTeacher getExerciseCatagory=retrofit.create(InterfaceTeacher.class);
        final Call<ExerciseCatagory> call=getExerciseCatagory.getExerciseCatagory(StaticInfo.currentLessonId);
        call.enqueue(new Callback<ExerciseCatagory>() {
            @Override
            public void onResponse(Call<ExerciseCatagory> call, Response<ExerciseCatagory> response) {
                View noTest=(LinearLayout)findViewById(R.id.layout_no_test);
                View hasTest=(LinearLayout)findViewById(R.id.layout_has_test);
                if(response.body()!=null){
                    list.add(response.body());
                    mAdapter.setData(list);
                    hasTest.setVisibility(View.VISIBLE);
                    noTest.setVisibility(View.INVISIBLE);
                }else{
                    noTest.setVisibility(View.VISIBLE);
                    hasTest.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onFailure(Call<ExerciseCatagory> call, Throwable t) {
                alertDialog.showAlertDialgo(ExerciseCatagoryListActivity.this,t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(resultCode==0){
                initView();
                initData();
            }
        }
    }
}
