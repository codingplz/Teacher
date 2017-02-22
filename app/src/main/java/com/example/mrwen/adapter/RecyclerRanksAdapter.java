package com.example.mrwen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.GlideCircleTransform;
import com.example.mrwen.Utils.UiUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.bean.Rank;
import com.example.mrwen.bean.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;



/**
 * Created by fate on 2017/2/13.
 */

public class RecyclerRanksAdapter extends BaseRecyclerViewAdapter<Rank,RecyclerRanksAdapter.RanksViewHolder> {
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    public RecyclerRanksAdapter(ArrayList<Rank> data) {
        super(data);
    }

    @Override
    public RanksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_ranks,parent, false);

        return new RanksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RanksViewHolder holder, int position) {
        Rank rank = data.get(position);
        User ranker = rank.getRanker();
        Glide.with(UiUtils.getContext())
                .load(UiUtils.getContext().getResources().getString(R.string.baseURL)+ ranker.getImageURL())
                .placeholder(R.drawable.default_photo)
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .into(holder.ivImage);
        holder.tvName.setText(ranker.getNickname());
        holder.tvContent.setText(rank.getContent());
        holder.tvTime.setText(mFormat.format(rank.getTime()));
        holder.rbRating.setRating(rank.getRank());
    }

    class RanksViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvName;
        RatingBar rbRating;
        TextView tvTime;
        TextView tvContent;

        public RanksViewHolder(View itemView) {
            super(itemView);
            ivImage = ButterKnife.findById(itemView, R.id.iv_ranker_image);
            tvName = ButterKnife.findById(itemView, R.id.tv_ranker_name);
            rbRating = ButterKnife.findById(itemView, R.id.rating_rank);
            tvTime = ButterKnife.findById(itemView, R.id.tv_time);
            tvContent = ButterKnife.findById(itemView, R.id.tv_rank_content);
        }
    }
}
