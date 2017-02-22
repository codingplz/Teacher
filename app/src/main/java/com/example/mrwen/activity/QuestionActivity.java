package com.example.mrwen.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.adapter.RecyclerAnswerAdapter;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.User;
import com.example.mrwen.interfaces.InterfaceQuestion;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.FullyLinearLayoutManager;
import com.example.mrwen.view.OnUserInfoClickListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.etv_question_content)
    ExpandableTextView tvDetail;
    @Bind(R.id.tv_discover_number)
    TextView tvDiscoverNumber;
    @Bind(R.id.recycler_answer)
    RecyclerView recyclerAnswer;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ll_asker)
    LinearLayout llAsker;
    private RecyclerAnswerAdapter adapter;
    private int mIid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        initview();

    }

    private void initview() {
        adapter = new RecyclerAnswerAdapter(new ArrayList<Answer>());
        recyclerAnswer.setLayoutManager(new FullyLinearLayoutManager(QuestionActivity.this));
        recyclerAnswer.setAdapter(adapter);
        adapter.setOnAgreeClickListener(new RecyclerAnswerAdapter.OnAgreeClickListener() {
            @Override
            public void onAgreeClick(int aid) {
                agreeAnswer(aid);
            }
        });
        adapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent = new Intent(QuestionActivity.this, PersonalInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        Issue issue = (Issue) getIntent().getSerializableExtra("issue");
        mIid = issue.getId();
        tvTitle.setText(issue.getTitle());
        tvDetail.setText(issue.getContent());
        tvDiscoverNumber.setText(issue.getAnswerNumber() + "人回答");
        if (issue.isAnonymous()) {
            tvName.setText("匿名提问");
            Glide.with(UiUtils.getContext())
                    .load(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(ivImage);
        } else {
            final User user = issue.getUser();
            tvName.setText(user.getNickname());
            Glide.with(UiUtils.getContext()).load(UiUtils.getContext().getResources().getString(R.string.baseURL) + user.getImageURL()).placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(ivImage);
            llAsker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuestionActivity.this, PersonalInfoActivity.class);
                    intent.putExtra("uid","s"+ user.getId());
                    startActivity(intent);
                }
            });
        }
        loadAnswers(mIid);
    }

    private void loadAnswers(final int iid) {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceQuestion loadAnswer=retrofit.create(InterfaceQuestion.class);
        final Call<ArrayList<Answer>> call=loadAnswer.loadAnswers(iid);
        call.enqueue(new Callback<ArrayList<Answer>>() {
            @Override
            public void onResponse(Call<ArrayList<Answer>> call, Response<ArrayList<Answer>> response) {
                adapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Answer>> call, Throwable t) {
                alertDialog.showAlertDialgo(QuestionActivity.this,t.toString());
            }
        });
    }


    private void agreeAnswer(int aid) {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceQuestion agreeAnswer=retrofit.create(InterfaceQuestion.class);
        final Call<Result> call=agreeAnswer.agreeAnswer(aid);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body().getCode()==0) {
                    new AlertDialog.Builder(QuestionActivity.this).setTitle("提示")
                            .setMessage("点赞成功")
                            .setPositiveButton("确认", null)
                            .show();
                }
                else{
                    new AlertDialog.Builder(QuestionActivity.this).setTitle("提示")
                            .setMessage("点赞失败")
                            .setPositiveButton("确认", null)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                alertDialog.showAlertDialgo(QuestionActivity.this,t.toString());
            }
        });
    }


    @OnClick({R.id.ib_answer, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_answer:
                Intent intent = new Intent(this, WriteAnswerActivity.class);
                intent.putExtra("iid", mIid);
                startActivityForResult(intent, 1);
                break;
            case R.id.ib_share:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)
            refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void refresh() {
        loadAnswers(mIid);
    }


}
