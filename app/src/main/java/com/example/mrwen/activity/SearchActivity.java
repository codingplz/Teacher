package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.RecyclerQueryContactAdapter;
import com.example.mrwen.adapter.RecyclerQueryResultAdapter;
import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {


    @Bind(R.id.recycler_search)
    RecyclerView recyclerSearch;

    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    RecyclerQueryContactAdapter mAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    private Spinner mSpinner;
    private EditText mEtRemark;
    private EditText mEtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            String query = intent.getStringExtra("query");
            queryContact(query);
        }
    }


    //查询好友
    private void queryContact(String query) {
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher search=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<QueryItem>> call=search.queryContacts(StaticInfo.uid,query);
        call.enqueue(new Callback<ArrayList<QueryItem>>() {
            @Override
            public void onResponse(Call<ArrayList<QueryItem>> call, Response<ArrayList<QueryItem>> response) {
                mAdapter.setData(response.body());

                new AlertDialog.Builder(SearchActivity.this).setTitle("提示")
                        .setMessage("查找成功")
                        .setPositiveButton("确认",null)
                        .show();
            }
            @Override
            public void onFailure(Call<ArrayList<QueryItem>> call, Throwable t) {
                alertDialog.showAlertDialgo(SearchActivity.this,t.toString());
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final View dialogView = initDialogView();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerQueryContactAdapter(new ArrayList<QueryItem>());
        recyclerSearch.setAdapter(mAdapter);
        mAdapter.setOnAddFriendListener(new RecyclerQueryContactAdapter.OnAddFriendListener() {
            @Override
            public void onAddFriend(final Info info, final RecyclerQueryResultAdapter.MyViewHolder holder) {
                mEtRemark.setText(info.getNickname());
                new AlertDialog.Builder(SearchActivity.this).setView(dialogView)
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
        mAdapter.setOnQueryItemClickListener(new RecyclerQueryContactAdapter.OnQueryItemClickListener() {
            @Override
            public void onQueryItemClick(View v, Info data) {
                Intent intent = new Intent(SearchActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",data.getUid());
                startActivity(intent);
            }
        });
    }

    //添加好友
    private void addFriend(Info info, final RecyclerQueryResultAdapter.MyViewHolder holder) {
        String group = mSpinner.getSelectedItem().toString();
        String s = mEtRemark.getText().toString();
        String message = mEtMessage.getText().toString();
        if (TextUtils.isEmpty(s.trim()))
            s = info.getNickname();
        Log.i("uid", "addFriend: "+ StaticInfo.uid);

        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher requestFriend=retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=requestFriend.requestFriend(info.getUid(), StaticInfo.uid,group,s,message);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 0) {
                    holder.showAdded();
                    new AlertDialog.Builder(SearchActivity.this).setTitle("提示")
                            .setMessage("添加好友成功")
                            .setPositiveButton("确认",null)
                            .show();
                }
                else{
                    holder.showAddFriend();
                    new AlertDialog.Builder(SearchActivity.this).setTitle("提示")
                            .setMessage("添加好友失败")
                            .setPositiveButton("确认",null)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(SearchActivity.this,t.toString());
            }
        });


    }

    private View initDialogView() {
        View view = View.inflate(this, R.layout.dialog_add_friend, null);
        mEtRemark = ButterKnife.findById(view, R.id.et_remark);
        final EditText etGroup = ButterKnife.findById(view,R.id.et_add_group);
        mSpinner = ButterKnife.findById(view, R.id.spinner_roster);
        ImageView ivAdd = ButterKnife.findById(view,R.id.iv_add_group);
        mEtMessage = ButterKnife.findById(view, R.id.et_message);
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
                    Toast.makeText(SearchActivity.this, "请输入组名", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //获取分组
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
                alertDialog.showAlertDialgo(SearchActivity.this,t.toString());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }
}
