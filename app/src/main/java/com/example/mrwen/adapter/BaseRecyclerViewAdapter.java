package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.view.OnRecyclerViewItemClickListener;

import java.util.ArrayList;


/**
 * Created by fate on 2016/10/30.
 */

public abstract class BaseRecyclerViewAdapter<E,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ArrayList<E> data;
    protected OnRecyclerViewItemClickListener<E> mListener;
    protected int size;
    public BaseRecyclerViewAdapter(ArrayList<E> data) {
        this.data = data;
        size = data.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<E> listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
            return data.size();
    }

    public void setData(ArrayList<E> data) {
        this.data = data;
          UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addAll(ArrayList<E> data) {
        this.data.addAll(data);
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
