package com.example.mrwen.view;

import android.view.View;

import com.example.mrwen.otherclass.ChapterPartInfo;

/**
 * Created by mrwen on 2017/3/10.
 */

public interface OnChapterListMenuListener {
    void onChapterListMenuListener(int id,View v,int isSigned,ChapterPartInfo chapter);
}
