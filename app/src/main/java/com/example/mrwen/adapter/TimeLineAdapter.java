package com.example.mrwen.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mrwen.Utils.ViewUtils;
import com.example.mrwen.activity.R;
import com.example.mrwen.otherclass.DataModal;
import com.example.mrwen.staticClass.Level;
import com.example.mrwen.view.OnGetIdListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishratkhan on 24/02/16.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.RvViewHolder> {

    List<DataModal> data = new ArrayList<>();
    Context mContext;

    private OnGetIdListener mOnGetIdListener;
    public TimeLineAdapter(Context con) {
        mContext = con;
    }

    public void setGetIdListener(OnGetIdListener onGetIdListener) {
        mOnGetIdListener = onGetIdListener;
    }

//    public void clear(){
//        data.clear();
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RvViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        final DataModal dataModal = data.get(position);
        holder.tv.setText(dataModal.getName());
        holder.setLevel(dataModal.getLevel());
        holder.rv_item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnGetIdListener!=null&&dataModal.getLevel()==3){
                    switch (dataModal.getType()){
                        case 0:
                            mOnGetIdListener.onGetIdListener("c"+dataModal.getCourseLearning().getCourse().getId());
                            break;
                        case 1:
                            mOnGetIdListener.onGetIdListener("i"+dataModal.getIssue().getId());
                            break;
                        case 2:
                            mOnGetIdListener.onGetIdListener("a"+dataModal.getAnswer().getId());
                            break;
                        case 3:
                            break;
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(DataModal item) {
        data.add(item);
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        View itemView;
        View marker;
        CardView rv_item_card;


        public RvViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.rv_item_tv);
            rv_item_card=(CardView) itemView.findViewById(R.id.rv_item_card);
            marker =itemView.findViewById(R.id.marker);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void setLevel(int level) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            );

            if (level == Level.LEVEL_TWO) {
                params.setMarginStart(ViewUtils.getLevelOneMargin());
                marker.setBackground(ContextCompat.getDrawable(mContext,R.drawable.marker_c));
            } else if (level == Level.LEVEL_THREE) {
                params.setMarginStart(ViewUtils.getLevelTwoMargin());
                marker.setBackground(ContextCompat.getDrawable(mContext,R.drawable.marker_cc));
            }

            itemView.setLayoutParams(params);
        }
    }
}
