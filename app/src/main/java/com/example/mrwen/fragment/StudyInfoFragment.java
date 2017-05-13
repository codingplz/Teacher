package com.example.mrwen.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.TimeUtils;
import com.example.mrwen.Utils.ViewUtils;
import com.example.mrwen.activity.QuestionActivity;
import com.example.mrwen.activity.R;
import com.example.mrwen.activity.StudyInfoToAnswer;
import com.example.mrwen.activity.StudyInfoToCourseInfo;
import com.example.mrwen.adapter.TimeLineAdapter;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.CourseLearning;
import com.example.mrwen.bean.DayStudyInfo;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.MessageRecord;
import com.example.mrwen.interfaces.InterfaceStudent;
import com.example.mrwen.otherclass.DataModal;
import com.example.mrwen.staticClass.Level;
import com.example.mrwen.view.OnGetIdListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    RecyclerView rv;
    int id;
    String studentName;
    ArrayList<Integer> idArray;
    ArrayList<String> nameArray;
    TimeLineAdapter rvAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rvAdapter = new TimeLineAdapter(context);
    }


    ArrayList<MessageRecord> messageArrayList = new ArrayList<MessageRecord>();



    public StudyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyInfoFragment newInstance(String param1, String param2) {
        StudyInfoFragment fragment = new StudyInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_time_line, null);
        Bundle bundle = getArguments();
        id=bundle.getInt("id");
        studentName=bundle.getString("studentName");
        idArray=bundle.getIntegerArrayList("idArray");
        nameArray=bundle.getStringArrayList("nameArray");
        rv = (RecyclerView) view.findViewById(R.id.courseRecyclerview);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ViewUtils.handleVerticalLines(view.findViewById(R.id.view_line_2), view.findViewById(R.id.view_line_3));
        retrofitGetStudy(id);


        rvAdapter.setGetIdListener(new OnGetIdListener() {
            @Override
            public void onGetIdListener(String tid) {
                int id = Integer.parseInt(tid.substring(1));
                char c = tid.charAt(0);
                switch (c) {
                    case 'c':
                        Intent intentC = new Intent(getActivity(), StudyInfoToCourseInfo.class);
                        intentC.putExtra("courseId", id);
                        startActivity(intentC);
                        break;
                    case 'i':
                        retrofitGetIssue(id);
                        break;
                    case 'a':
                        Intent intentA = new Intent(getActivity(), StudyInfoToAnswer.class);
                        intentA.putExtra("answerId", id);
                        startActivity(intentA);
                }
            }
        });
        return view;
    }


    private void retrofitGetIssue(int id) {
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceStudent getSingleIssue = retrofit.create(InterfaceStudent.class);
        final Call<Issue> call = getSingleIssue.getSingleIssue(id);
        call.enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("issue", response.body());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                alertDialog.showAlertDialgo(getActivity(), t.toString());
            }
        });
    }

    private void retrofitGetStudy(int id) {
        //rvAdapter.clear();
        final MyDialog alertDialog = new MyDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceStudent getStudyInfo = retrofit.create(InterfaceStudent.class);

        final Call<ArrayList<DayStudyInfo>> call = getStudyInfo.getStudyInfo(id);
        call.enqueue(new Callback<ArrayList<DayStudyInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<DayStudyInfo>> call, Response<ArrayList<DayStudyInfo>> response) {
                if (response.body().size() != 0) {
                    for (DayStudyInfo dsi : response.body()) {
                        rvAdapter.addItem(new DataModal(Level.LEVEL_ONE, TimeUtils.Format(dsi.getTime())));
                        if (dsi.getLearnings().size() != 0) {
                            rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "观看课程"));
                            for (CourseLearning cl : dsi.getLearnings()) {
                                DataModal dm = new DataModal(Level.LEVEL_THREE, 0, cl.getCourse().getName() + " " + cl.getDuration() / 60000 + "分钟");
                                dm.setCourseLearning(cl);
                                rvAdapter.addItem(dm);
                            }
                        }
                        if (dsi.getIssues().size() != 0) {
                            rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "提出问题"));
                            for (Issue i : dsi.getIssues()) {
                                DataModal dm = new DataModal(Level.LEVEL_THREE, 1, i.getTitle());
                                dm.setIssue(i);
                                rvAdapter.addItem(dm);
                            }
                        }
                        if (dsi.getAnswers().size() != 0) {
                            rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "回答问题"));
                            for (Answer a : dsi.getAnswers()) {
                                DataModal dm = new DataModal(Level.LEVEL_THREE, 2, a.getIssue().getTitle());
                                dm.setAnswer(a);
                                rvAdapter.addItem(dm);
                            }
                        }
                        if (dsi.getMessages().size() != 0) {
                            rvAdapter.addItem(new DataModal(Level.LEVEL_TWO, "在线交流"));
                            rvAdapter.addItem(new DataModal(Level.LEVEL_THREE,3, "发出消息 " + dsi.getMessages().size() + " 次"));
                        }
                    }
                    rv.setAdapter(rvAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DayStudyInfo>> call, Throwable t) {
                alertDialog.showAlertDialgo(getActivity(), t.toString());
            }
        });
    }

}