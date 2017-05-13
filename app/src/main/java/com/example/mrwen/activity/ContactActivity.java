package com.example.mrwen.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.ExpandableContactAdapter;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.Roster;
import com.example.mrwen.bean.RosterGroup;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.AnimatedExpandableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.aelv_contact)
    AnimatedExpandableListView aelvContact;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    private ExpandableContactAdapter mAdapter;
    private Gson gson = new Gson();
    private String cachePath;
    private File cacheFile;
    private ArrayList<RosterGroup> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        loadData();
    }

    private void initView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(ContactActivity.this, SearchActivity.class);
                intent.putExtra("query", query);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, StaticInfo.REQURST_SEARCH);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mAdapter = new ExpandableContactAdapter(new ArrayList<RosterGroup>());
        aelvContact.setAdapter(mAdapter);
        aelvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Roster roster = (Roster) mAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(Intent.ACTION_VIEW, StaticInfo.getPrivateChatUri(roster.getUid(), roster.getRemark()));
                startActivity(intent);
                return false;
            }
        });
    }

    public void loadData() {
        cachePath = getCacheDir().getAbsolutePath();
        cacheFile = new File(cachePath + File.separator + getClass().getSimpleName());
        if (cacheFile.exists()){

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(cacheFile));
                String line ;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                    data = gson.fromJson(builder.toString(), new TypeToken<ArrayList<RosterGroup>>(){}.getType());
                }
                mAdapter.setData(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        retrofitGetContacts();
        retrofitGetRequestCount();
    }


    private void retrofitGetRequestCount(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getRequestCount=retrofit.create(InterfaceTeacher.class);
        final Call<Result> call=getRequestCount.getRequestCount(StaticInfo.uid);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body()!=null)
                    tvNumber.setText(response.body().getCount()+"");
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                alertDialog.showAlertDialgo(ContactActivity.this,t.toString());
            }
        });
    }

    private void retrofitGetContacts(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getContacts=retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<RosterGroup>> call=getContacts.getContacts(StaticInfo.uid);
        call.enqueue(new Callback<ArrayList<RosterGroup>>() {
            @Override
            public void onResponse(Call<ArrayList<RosterGroup>> call, Response<ArrayList<RosterGroup>> response) {
                if(response.body()!=null) {
                    mAdapter.setData(response.body());
                    StaticInfo.groupNames.clear();
                    for (RosterGroup group : response.body()) {
                        StaticInfo.groupNames.add(group.getName());
                    }
                    writeToCache(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<RosterGroup>> call, Throwable t) {
                new AlertDialog.Builder(ContactActivity.this).setMessage("获取数据异常:" + t.toString()).setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadDataFromServer();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
    }


    private void writeToCache(ArrayList<RosterGroup> groups) {
        data = groups;
        PrintWriter writer = null;
        try {
            writer =new PrintWriter( cacheFile) ;
            writer.print(gson.toJson(groups));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticInfo.REQURST_SEARCH && resultCode == RESULT_OK) {
            loadDataFromServer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.rl_friend_request, R.id.rl_my_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_friend_request:
                Intent intent = new Intent(this, FriendRequestActivity.class);
                startActivityForResult(intent, StaticInfo.REQURST_SEARCH);
                break;
            case R.id.rl_my_group:
                Intent groupIntent = new Intent(this, GroupsActivity.class);
                startActivity(groupIntent);
                break;
        }
    }

}
