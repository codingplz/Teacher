package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrwen.Utils.ActivityManager;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerExerciseUploadAdapter;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseUploadActivity extends AppCompatActivity {

    @Bind(R.id.recycler_exercise_upload)
    RecyclerView recycler_exercise_upload;
    private RecyclerExerciseUploadAdapter mAdapter;
    ArrayList<Integer> exercises = new ArrayList<>();
    ArrayList<Integer> hasChecked = new ArrayList<>();
    Button button;
    ArrayList<Integer> idArray = new ArrayList<>();
    int TEST_CHECK = 1;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_upload);
        ButterKnife.bind(this);
        hasChecked=getIntent().getIntegerArrayListExtra("exercises");
        type=getIntent().getStringExtra("type");
        initView();
        initData();
    }

    public void initView() {
        mAdapter = new RecyclerExerciseUploadAdapter(new ArrayList<Exercise>(),exercises,hasChecked);
        recycler_exercise_upload.setLayoutManager(new LinearLayoutManager(this));
        recycler_exercise_upload.setAdapter(mAdapter);
        button = (Button) findViewById(R.id.bt_exercise_upload);
        //选中题目的监听
        mAdapter.setExerciseCheckListener(new OnRecyclerViewItemClickListener<Exercise>() {
            @Override
            public void onItemClick(View v, Exercise data) {
                ImageView iv = (ImageView) v.findViewById(R.id.iv_test_check);

                //是否已经被选中
                Boolean isChecked = false;
                for (int i : exercises) {
                    if (i == data.getId())
                        isChecked = true;
                }
                if (!isChecked) {
                    exercises.add(data.getId());
                    iv.setImageResource(R.drawable.ic_check_circle_red_500_24dp);
                } else {
                    exercises.remove(exercises.indexOf(data.getId()));
                    iv.setImageResource(R.drawable.ic_check_circle_grey_400_24dp);
                }
                button.setText("已选题目（" + exercises.size() + "）");
            }
        });



        //查看题目
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Exercise>() {
            @Override
            public void onItemClick(View v, Exercise data) {
                Intent intent = new Intent(ExerciseUploadActivity.this, TestCheckActivity.class);
                intent.putExtra("exerciseId", data.getId());
                intent.putIntegerArrayListExtra("exercises", exercises);
                if(type.equals("add")){
                    for(int i:hasChecked){
                        idArray.remove((Integer)i);
                    }
                }
                intent.putIntegerArrayListExtra("idArray", idArray);
                intent.putExtra("type","upload");
                startActivityForResult(intent, TEST_CHECK);
            }
        });

        //上传题目
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                StringBuffer sb=new StringBuffer();
                for(int i:exercises){
                    sb.append(String.valueOf(i));
                }
                map.put("exercises",sb.toString());
                map.put("lessonId",StaticInfo.currentLessonId);
                map.put("type",type);
                retorfitUplpadTest(map);
            }
        });
    }

    private void retorfitUplpadTest(Map map){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher uploadTest=retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=uploadTest.uploadTest(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null) {
                    if(type.equals("upload")) {
                        new android.support.v7.app.AlertDialog.Builder(ExerciseUploadActivity.this).setTitle("提示")
                                .setMessage("上传题目成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent();
                                        setResult(0,intent);
                                        finish();
                                    }
                                })
                                .show();
                    }else if(type.equals("add")){
                        new android.support.v7.app.AlertDialog.Builder(ExerciseUploadActivity.this).setTitle("提示")
                                .setMessage("添加题目成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent();
                                        setResult(1,intent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(ExerciseUploadActivity.this,t.toString());
            }
        });
    }


    private void initData() {
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getTest = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Exercise>> call = getTest.getTest("l" + StaticInfo.currentLessonId);
        call.enqueue(new Callback<ArrayList<Exercise>>() {
            @Override
            public void onResponse(Call<ArrayList<Exercise>> call, Response<ArrayList<Exercise>> response) {
                if (response.body() != null) {
                    mAdapter.setData(response.body());
                    for (Exercise e : response.body()) {
                        idArray.add(e.getId());
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Exercise>> call, Throwable t) {
                alertDialog.showAlertDialgo(ExerciseUploadActivity.this, t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if(requestCode==1){
                exercises = data.getIntegerArrayListExtra("exercises");
                initView();
                initData();
                button.setText("已选题目（" + exercises.size() + "）");
            }
        }
    }
}
