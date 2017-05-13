package com.example.mrwen.view;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by fate on 2016/10/30.
 */

public interface OnRecyclerViewItemClickListener<E> {
    void onItemClick(View v, E data);

}
