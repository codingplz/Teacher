package com.example.mrwen.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.activity.R;
import com.example.mrwen.activity.TestCheckActivity;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.MessageRecord;
import com.example.mrwen.interfaces.InterfaceTeacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseCheckFragment extends Fragment {

    int id;
    private TextView title,optionA,optionB,optionC,optionD,opA,opB,opC,opD,answer,detail,analysis;
    private FloatingActionButton fab;
    Boolean isChecked=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    ArrayList<MessageRecord> messageArrayList = new ArrayList<MessageRecord>();

    public ExerciseCheckFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_check, null);
        title=(TextView) view.findViewById(R.id.tv_test_title);
        optionA=(TextView) view.findViewById(R.id.tv_test_optionA);
        optionB=(TextView) view.findViewById(R.id.tv_test_optionB);
        optionC=(TextView) view.findViewById(R.id.tv_test_optionC);
        optionD=(TextView) view.findViewById(R.id.tv_test_optionD);
        opA=(TextView) view.findViewById(R.id.tv_test_opA);
        opB=(TextView) view.findViewById(R.id.tv_test_opB);
        opC=(TextView) view.findViewById(R.id.tv_test_opC);
        opD=(TextView) view.findViewById(R.id.tv_test_opD);
        answer=(TextView) view.findViewById(R.id.tv_exer_check_answer);
        detail=(TextView) view.findViewById(R.id.tv_exer_check_detail);
        analysis=(TextView) view.findViewById(R.id.tv_exer_check_analysis);
        fab=(FloatingActionButton)view.findViewById(R.id.fab_add_test);

        Bundle bundle = getArguments();
        id=bundle.getInt("id");
        retrofitGetExercise(id);

        final TestCheckActivity tca=(TestCheckActivity) getActivity();

        //是否已被选入
        for(int i:tca.exercises){
            if(id==i) {
                isChecked=true;
                fab.setImageResource(R.drawable.ic_check_circle_red_500_24dp);
            }
        }


        //悬浮选中按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    fab.setImageResource(R.drawable.ic_check_circle_grey_400_24dp);
                    tca.exercises.remove(tca.exercises.indexOf(id));
                    isChecked=false;
                }else{
                    fab.setImageResource(R.drawable.ic_check_circle_red_500_24dp);
                    tca.exercises.add(id);
                    isChecked=true;
                }
            }
        });
        if(bundle.get("type").equals("check"))
            fab.setVisibility(View.INVISIBLE);
        return view;
    }

    private void retrofitGetExercise(int id){
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getSingleExercise = retrofit.create(InterfaceTeacher.class);
        final Call<Exercise> call = getSingleExercise.getSingleExercise(String.valueOf(id));
        call.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                Exercise e=response.body();
                if(e!=null) {
                    title.setText(e.getTitle());
                    optionA.setText(e.getOptionA());
                    optionB.setText(e.getOptionB());
                    optionC.setText(e.getOptionC());
                    optionD.setText(e.getOptionD());
                    switch (e.getAnswer()){
                        case '1':
                            opA.setBackgroundResource(R.drawable.answer_circle);
                            answer.setText("A");
                            opA.setTextColor(Color.WHITE);
                            break;
                        case '2':
                            opB.setBackgroundResource(R.drawable.answer_circle);
                            answer.setText("B");
                            opB.setTextColor(Color.WHITE);
                            break;
                        case '3':
                            opC.setBackgroundResource(R.drawable.answer_circle);
                            answer.setText("C");
                            opC.setTextColor(Color.WHITE);
                            break;
                        default:
                            opD.setBackgroundResource(R.drawable.answer_circle);
                            answer.setText("D");
                            opD.setTextColor(Color.WHITE);
                    }
                    detail.setText("本题共被作答"+e.getNumber()+"次，正确率为"+e.getAccuracy()+"%，难度"+e.getDifficulty()+"。");
                    analysis.setText(e.getAnalysis());
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                alertDialog.showAlertDialgo(getActivity(), t.toString());
            }
        });
    }

}