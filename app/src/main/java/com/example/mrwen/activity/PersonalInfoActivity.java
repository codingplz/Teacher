package com.example.mrwen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.bean.InfoDetail;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonalInfoActivity extends AppCompatActivity {

    @Bind(R.id.iv_person_info_photo)
    ImageView mIvPersonInfoPhoto;
    @Bind(R.id.tv_person_info_name)
    TextView mTvPersonInfoName;
    @Bind(R.id.tv_person_info_gender)
    TextView mTvPersonInfoGender;
    @Bind(R.id.tv_person_info_rank)
    TextView mTvPersonInfoRank;
    @Bind(R.id.tv_person_info_region)
    TextView mTvPersonInfoRegion;
    @Bind(R.id.tv_person_info_signature)
    TextView mTvPersonInfoSignature;
    @Bind(R.id.tv_person_info_phone)
    TextView mTvPersonInfoPhone;
    @Bind(R.id.tv_person_info_email)
    TextView mTvPersonInfoEmail;
    @Bind(R.id.tv_person_info_identity)
    TextView mTvPersonInfoIdentity;
    @Bind(R.id.tv_person_info_school)
    TextView mTvPersonInfoSchool;

    private String uid;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        uid = getIntent().getStringExtra("uid");
        initData();
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getDetailedInfo=retrofit.create(InterfaceTeacher.class);
        final Call<InfoDetail> call=getDetailedInfo.getDetailedInfo(uid);
        call.enqueue(new Callback<InfoDetail>() {
            @Override
            public void onResponse(Call<InfoDetail> call, Response<InfoDetail> response) {
                populateInfo(response.body());
            }
            @Override
            public void onFailure(Call<InfoDetail> call, Throwable t) {
                alertDialog.showAlertDialgo(PersonalInfoActivity.this,t.toString());
            }
        });
    }

    private void populateInfo(InfoDetail detail) {
        nickname = detail.getNickname();
        setText(mTvPersonInfoName, nickname);
        setText(mTvPersonInfoEmail, detail.getEmail());
        setText(mTvPersonInfoGender, detail.getGender());
        setText(mTvPersonInfoPhone, detail.getPhone());
        setText(mTvPersonInfoRegion, detail.getRegion());
        setText(mTvPersonInfoSignature, detail.getSignature());
        setText(mTvPersonInfoIdentity, StaticInfo.getIdentity(uid));
        setText(mTvPersonInfoSchool, detail.getSchool());
        Glide.with(UiUtils.getContext()).load(getResources().getString(R.string.baseURL) + detail.getImageURL())
                .placeholder(R.drawable.default_photo)
                .transform(new GlideCircleTransform(this))
                .into(mIvPersonInfoPhoto);
    }

    private void setText(TextView textView, String text) {
        if (TextUtils.isEmpty(text))
            textView.setText("未填写");
        else
            textView.setText(text);
    }

    @OnClick(R.id.bt_chat)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW, StaticInfo.getPrivateChatUri(uid,nickname));
        startActivity(intent);
    }

}
