package com.example.mrwen.view;

import android.view.View;

import com.example.mrwen.otherclass.ChapterPartInfo;

/**
 * Created by mrwen on 2017/3/9.
 */

public interface OnChapterListClickListener {
    void onChapterListClick(View v, ChapterPartInfo data,int position);
}
