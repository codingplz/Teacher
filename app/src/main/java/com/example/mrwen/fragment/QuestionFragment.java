package com.example.mrwen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.QuestionActivity;
import com.example.mrwen.activity.WriteAnswerActivity;
import com.example.mrwen.adapter.RecyclerDiscoverAdapter;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.interfaces.InterfaceQuestion;
import com.example.mrwen.activity.R;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/10/29.
 */

public class QuestionFragment extends BaseFragment {
    private int start = 0;
    private RecyclerView mRecyclerView;
    ArrayList<Issue> data=new ArrayList<>();
    private RecyclerDiscoverAdapter mAdapter=new RecyclerDiscoverAdapter(data);
    TwinklingRefreshLayout refreshLayout;


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_discover);
        refreshLayout=(TwinklingRefreshLayout)view.findViewById(R.id.trl_discover);
        refreshLayout.setEnableRefresh(true);
        retrofitRefresh();
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //refreshLayout.startRefresh();
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                retrofitRefresh();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                retrofitLoadMore();
            }
        });




        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Issue>() {
            @Override
            public void onItemClick(View v, Issue data) {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("issue",data);
                startActivity(intent);
            }
        });
        mAdapter.setOnAnswerClickListener(new RecyclerDiscoverAdapter.OnAnswerClickListener() {
            @Override
            public void onAnswerClick(int iid) {
                Intent intent = new Intent(getActivity(), WriteAnswerActivity.class);
                intent.putExtra("iid",iid);
                startActivity(intent);
            }
        });

    }

    private void retrofitRefresh(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceQuestion loadDiscover=retrofit.create(InterfaceQuestion.class);
        final Call<ArrayList<Issue>> call=loadDiscover.loadDiscover(0);
        call.enqueue(new Callback<ArrayList<Issue>>() {
            @Override
            public void onResponse(Call<ArrayList<Issue>> call, Response<ArrayList<Issue>> response) {


                if (response.body().size()!=0) {
                    if (response.body().size()<10){
                        refreshLayout.setEnableLoadmore(false);
                    }
                    mAdapter.setData(response.body());
                }
                else{
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Issue>> call, Throwable t) {
                alertDialog.showAlertDialgo(UiUtils.getContext(),t.toString());
            }
        });
    }
    private void retrofitLoadMore(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceQuestion loadDiscover=retrofit.create(InterfaceQuestion.class);
        final Call<ArrayList<Issue>> call=loadDiscover.loadDiscover(start);
        call.enqueue(new Callback<ArrayList<Issue>>() {
            @Override
            public void onResponse(Call<ArrayList<Issue>> call, Response<ArrayList<Issue>> response) {
                if (response.body().size()<10){
                    refreshLayout.setEnableLoadmore(false);
                    Toast.makeText(QuestionFragment.this.getActivity(), "已无更多", Toast.LENGTH_SHORT).show();
                }
                start+=response.body().size();
                mAdapter.addAll(response.body());
                refreshLayout.finishLoadmore();
            }
            @Override
            public void onFailure(Call<ArrayList<Issue>> call, Throwable t) {
                alertDialog.showAlertDialgo(UiUtils.getContext(),t.toString());
            }
        });
    }

}
