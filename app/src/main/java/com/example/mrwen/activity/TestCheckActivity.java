package com.example.mrwen.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mrwen.adapter.ExerciseCheckFragmentAdapter;
import com.example.mrwen.adapter.FragAdapter;
import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.fragment.ExerciseCheckFragment;
import com.example.mrwen.fragment.StudyInfoFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TestCheckActivity extends AppCompatActivity {

    int id;
    String type;
    ArrayList<Integer> idArray;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ViewPager viewPager;
    private ExerciseCheckFragmentAdapter adapter;
    public ArrayList<Integer> exercises=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_check);
        type=getIntent().getStringExtra("type");
        idArray=getIntent().getIntegerArrayListExtra("idArray");
        id=getIntent().getIntExtra("exerciseId",0);
        exercises=getIntent().getIntegerArrayListExtra("exercises");
        manager = getSupportFragmentManager();
        viewPager=(ViewPager)findViewById(R.id.viewpager);

        ArrayList<ExerciseCheckFragment> fragments = new ArrayList<ExerciseCheckFragment>();
        for(int i=0;i<idArray.size();i++){
            ExerciseCheckFragment fragment= new ExerciseCheckFragment();
//            if(type.equals("check")) {
//                View v= fragment.getView();
//                FloatingActionButton fab=(FloatingActionButton)v.findViewById(R.id.fab_add_test);
//                fab.setVisibility(View.INVISIBLE);
//            }
            Bundle bundle = new Bundle();
            bundle.putInt("id",idArray.get(i));
            bundle.putString("type",type);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        adapter = new ExerciseCheckFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(idArray.indexOf(id));
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putIntegerArrayListExtra("exercises",exercises);
        setResult(2,intent);
        finish();
        super.onBackPressed();
    }
}
