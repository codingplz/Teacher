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


import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.adapter.ExpandableContactAdapter;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.Roster;
import com.example.mrwen.bean.RosterGroup;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;
import com.example.mrwen.view.AnimatedExpandableListView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(ContactActivity.this, SearchActivity.class);
                intent.putExtra("query", query);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,1);
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
                Intent intent = new Intent(Intent.ACTION_VIEW, StaticInfo.getPrivateChatUri(roster.getUid(),roster.getRemark()));
                startActivity(intent);
                return false;
            }
        });
    }

    private void initData() {
        retrofitGetRequestCount();
        retrofitGetContacts();
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
                mAdapter.setData(response.body());
                StaticInfo.groupNames.clear();
                for (RosterGroup group : response.body()) {
                    StaticInfo.groupNames.add(group.getName());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<RosterGroup>> call, Throwable t) {
                alertDialog.showAlertDialgo(ContactActivity.this,t.toString());
            }
        });
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
                tvNumber.setText(response.body().getCount()+"");
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                alertDialog.showAlertDialgo(ContactActivity.this,t.toString());
            }
        });
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.rl_friend_request)
    public void onClick() {
        Intent intent = new Intent(this, FriendRequestActivity.class);
        startActivityForResult(intent, 1);
    }

}
