package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.mrwen.adapter.FragAdapter;
import com.example.mrwen.fragment.StudyInfoFragment;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyTimeLineActivity extends AppCompatActivity {
    RecyclerView rv;
    int id;
    String studentName;
    ArrayList<Integer> idArray;
    ArrayList<String> nameArray;

//    float x1 = 0;
//    float x2 = 0;
//    float y1 = 0;
//    float y2 = 0;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ViewPager viewPager;
    private FragAdapter adapter;
    private ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_time_line);

        Intent intent=getIntent();
        //View view=(RelativeLayout)findViewById(R.id.rl_root);
        id=intent.getIntExtra("studentId",0);
        studentName=intent.getStringExtra("studentName");
        idArray=intent.getIntegerArrayListExtra("idArray");
        nameArray=intent.getStringArrayListExtra("nameArray");

        manager = getSupportFragmentManager();

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        actionBar=this.getSupportActionBar();

        ArrayList<StudyInfoFragment> fragments = new ArrayList<StudyInfoFragment>();

        for(int i=0;i<idArray.size();i++){
            StudyInfoFragment fragment= new StudyInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id",idArray.get(i));
            bundle.putString("studentName",nameArray.get(i));
            bundle.putIntegerArrayList("idArray",idArray);
            bundle.putStringArrayList("nameArray",nameArray);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                actionBar.setTitle(nameArray.get(position));
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


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if(y1 - y2 > 50) {
//            } else if(y2 - y1 > 50) {
//            } else if(x1 - x2 > 50) {
//                //向左滑
//                int index=idArray.indexOf(id);
//                int newIndex=(index+1+idArray.size())%idArray.size();
//                Log.i("indexxxx1",index+"_"+newIndex);
//                retrofitGetStudy(idArray.get(newIndex));
//                id=idArray.get(newIndex);
//                Intent intent=new Intent(this,StudyTimeLineActivity.class);
//                intent.putExtra("studentId",id);
//                intent.putExtra("studentName",nameArray.get(newIndex));
//                intent.putExtra("nameArray",nameArray);
//                intent.putExtra("idArray",idArray);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.anim_enter,R.anim.anim_enter);
//                return true;
//            } else if(x2 - x1 > 50) {
//                //向右滑
//                int index=idArray.indexOf(id);
//                int newIndex=(index-1+idArray.size())%idArray.size();
//                retrofitGetStudy(idArray.get(newIndex));
//                id=idArray.get(newIndex);
//                Intent intent=new Intent(this,StudyTimeLineActivity.class);
//                intent.putExtra("studentId",id);
//                intent.putExtra("studentName",nameArray.get(newIndex));
//                intent.putExtra("nameArray",nameArray);
//                intent.putExtra("idArray",idArray);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
//                return true;
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }



//    private void retrofitGetStudy(){
//        final MyDialog alertDialog=new MyDialog();
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(getResources().getString(R.string.baseURL))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        final InterfaceStudent getCourseLearning=retrofit.create(InterfaceStudent.class);
//
//        final Call<ArrayList<CourseLearning>> call=getCourseLearning.getCourseLearning(id);
//        call.enqueue(new Callback<ArrayList<CourseLearning>>() {
//            @Override
//            public void onResponse(Call<ArrayList<CourseLearning>> call, Response<ArrayList<CourseLearning>> response) {
//                courseLearningArrayList=response.body();
//                retrofitGetMessage();
//            }
//            @Override
//            public void onFailure(Call<ArrayList<CourseLearning>> call, Throwable t) {
//                alertDialog.showAlertDialgo(StudyTimeLineActivity.this,t.toString());
//            }
//        });
//    }

//    private void retrofitGetMessage(){
//        final MyDialog alertDialog=new MyDialog();
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(getResources().getString(R.string.baseURL))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        final InterfaceStudent getMessageRecord=retrofit.create(InterfaceStudent.class);
//
//        final Call<ArrayList<MessageRecord>> call=getMessageRecord.getMessageRecord("s"+id);
//        call.enqueue(new Callback<ArrayList<MessageRecord>>() {
//            @Override
//            public void onResponse(Call<ArrayList<MessageRecord>> call, Response<ArrayList<MessageRecord>> response) {
//                messageArrayList=response.body();
//                Date tempTime=null;
//                Date time;
//                for(CourseLearning cl:courseLearningArrayList){
//                    time=cl.getTime();
//                    if(!time.equals(tempTime)){
//                        rvAdapter.addItem(new DataModal(Level.LEVEL_ONE, TimeUtils.Format(cl.getTime())));
//                        rvAdapter.addItem(new DataModal(Level.LEVEL_TWO,cl.getCourse().getName()+" "+cl.getDuration()/60000+"分钟"));
//                    }else{
//                        rvAdapter.addItem(new DataModal(Level.LEVEL_TWO,cl.getCourse().getName()+" "+cl.getDuration()/60000+"分钟"));
//                    }
//                    tempTime=time;
//                }
//                rv.setAdapter(rvAdapter);
//            }
//            @Override
//            public void onFailure(Call<ArrayList<MessageRecord>> call, Throwable t) {
//                alertDialog.showAlertDialgo(StudyTimeLineActivity.this,t.toString());
//            }
//        });
//    }
}
