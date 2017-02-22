package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerFriendRequestAdapter;
import com.example.mrwen.bean.FriendRequest;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FriendRequestActivity extends AppCompatActivity {

    @Bind(R.id.re_friend_request)
    RecyclerView reFriendRequest;
    private RecyclerFriendRequestAdapter mAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    private Spinner mSpinner;
    private EditText mEtRemark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initData() {

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getFriendRequest=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<FriendRequest>> call=getFriendRequest.getFriendRequest(StaticInfo.uid);
        call.enqueue(new Callback<ArrayList<FriendRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendRequest>> call, Response<ArrayList<FriendRequest>> response) {
                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<FriendRequest>> call, Throwable t) {
                alertDialog.showAlertDialgo(FriendRequestActivity.this,t.toString());
            }
        });
    }

    private void initView() {
        reFriendRequest.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerFriendRequestAdapter(new ArrayList<FriendRequest>());
        reFriendRequest.setAdapter(mAdapter);
        final View dialogView = initDialogView();
        mAdapter.setOnAddFriendListener(new RecyclerFriendRequestAdapter.OnAddFriendListener() {
            @Override
            public void onAddFriend(final FriendRequest info, final RecyclerFriendRequestAdapter.MyViewHolder holder) {
                mEtRemark.setText(info.getNickname());
                new AlertDialog.Builder(FriendRequestActivity.this).setView(dialogView)
                        .setTitle("添加好友")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.showAdding();
                                addFriend(info,holder);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
    private View initDialogView() {
        View view = View.inflate(this, R.layout.dialog_accept_friend, null);
        mEtRemark = ButterKnife.findById(view, R.id.et_remark);
        final EditText etGroup = ButterKnife.findById(view,R.id.et_add_group);
        mSpinner = ButterKnife.findById(view, R.id.spinner_roster);
        ImageView ivAdd = ButterKnife.findById(view,R.id.iv_add_group);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, StaticInfo.groupNames);
        mSpinner.setAdapter(mArrayAdapter);
        if (StaticInfo.groupNames.size() == 0){
            getGroupNames();
        }
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etGroup.getText().toString();
                if (!TextUtils.isEmpty(str)){
                    mArrayAdapter.add(str);
                    mSpinner.setSelection(mArrayAdapter.getCount()-1);
                }else {
                    Toast.makeText(FriendRequestActivity.this, "请输入组名", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private void getGroupNames() {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getGroupNames=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<String>> call=getGroupNames.getGroupNames(StaticInfo.uid);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                StaticInfo.groupNames=response.body();
                mArrayAdapter.addAll(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                alertDialog.showAlertDialgo(FriendRequestActivity.this,t.toString());
            }
        });


    }
    private void addFriend(FriendRequest info, final RecyclerFriendRequestAdapter.MyViewHolder holder) {
        String group = mSpinner.getSelectedItem().toString();
        String s = mEtRemark.getText().toString();
        if (TextUtils.isEmpty(s.trim()))
            s = info.getNickname();

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher addFriend=retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=addFriend.addFriend(info.getId(),group,s);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 0) {
                    holder.showAdded();
                    setResult(RESULT_OK);
                }
                else{
                    holder.showAddFriend();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(FriendRequestActivity.this,t.toString());
            }
        });
    }
}
