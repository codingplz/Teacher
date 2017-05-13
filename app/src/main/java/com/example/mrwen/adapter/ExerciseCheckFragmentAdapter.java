package com.example.mrwen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mrwen.fragment.ExerciseCheckFragment;
import com.example.mrwen.fragment.StudyInfoFragment;

import java.util.ArrayList;

/**
 * Created by mrwen on 2017/4/15.
 */

public class ExerciseCheckFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<ExerciseCheckFragment> fragments;


    public ExerciseCheckFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ExerciseCheckFragmentAdapter(FragmentManager fm, ArrayList<ExerciseCheckFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}