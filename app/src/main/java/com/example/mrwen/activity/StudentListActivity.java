package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerStudentListAdapter;
import com.example.mrwen.bean.Student;
import com.example.mrwen.interfaces.InterfaceClass;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.OnChatListener;
import com.example.mrwen.view.OnGetIdListener;
import com.example.mrwen.view.OnStudyInfoClickListener;
import com.example.mrwen.view.OnUserInfoClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentListActivity extends AppCompatActivity {

    @Bind(R.id.universal_recycle_view)
    RecyclerView recyclerClass;

    private RecyclerStudentListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_recycle_list);
        ButterKnife.bind(this);
        initView();;
        initData();

        mAdapter.setOnStudyInfoListener(new OnStudyInfoClickListener() {
            @Override
            public void onStudyInfoClickListener(int id,String name,ArrayList<Integer> idArray,ArrayList<String> nameArray) {
                Intent intent=new Intent(StudentListActivity.this,StudyTimeLineActivity.class);
                intent.putExtra("studentId",id);
                intent.putExtra("studentName",name);
                intent.putExtra("idArray",idArray);
                intent.putExtra("nameArray",nameArray);
                startActivity(intent);
            }
        });

        mAdapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent=new Intent(StudentListActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        mAdapter.setmOnChatListener(new OnChatListener() {
            @Override
            public void onChatListener(String uid, String name) {
                Intent intent = new Intent(Intent.ACTION_VIEW, StaticInfo.getPrivateChatUri(uid,name));
                startActivity(intent);
            }
        });


//        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Student>() {
//            @Override
//            public void onItemClick(View v, Student data) {
//                Intent intent=new Intent(StudentListActivity.this,StudyTimeLineActivity.class);
//                intent.putExtra("studentId",data.getId());
//                startActivity(intent);
//            }
//        });
//        iv_study_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(StudentListActivity.this,StudyTimeLineActivity.class);
//                intent.putExtra("studentId",data.getId());
//                startActivity(intent);
//            }
//        });
    }

    private void initView() {
        mAdapter = new RecyclerStudentListAdapter(new ArrayList<Student>());
        recyclerClass.setLayoutManager(new LinearLayoutManager(this));
        recyclerClass.setAdapter(mAdapter);
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceClass getStudentList=retrofit.create(InterfaceClass.class);
        final Call<ArrayList<Student>> call=getStudentList.getStudentList(StaticInfo.currentClassId);
        call.enqueue(new Callback<ArrayList<Student>>() {
            @Override
            public void onResponse(Call<ArrayList<Student>> call, Response<ArrayList<Student>> response) {
                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Student>> call, Throwable t) {
                alertDialog.showAlertDialgo(StudentListActivity.this,t.toString());
            }
        });
    }
}
