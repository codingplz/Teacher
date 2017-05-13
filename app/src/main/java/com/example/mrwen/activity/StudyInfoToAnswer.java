package com.example.mrwen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.TimeUtils;
import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.interfaces.InterfaceStudent;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyInfoToAnswer extends AppCompatActivity {

    @Bind(R.id.layout_study_info_to_answer)
    LinearLayout layout_study_info_to_answer;

    @Bind(R.id.tv_issue_title)
    TextView tvTitle;
    @Bind(R.id.tv_answer_content)
    TextView tvContent;
    @Bind(R.id.tv_agree)
    TextView tvAgree;
    @Bind(R.id.tv_answer_name)
    TextView tvName;
    @Bind(R.id.iv_answer_image)
    ImageView ivImage;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_answer)
    LinearLayout llUser;

    Answer answer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_info_to_answer);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        final int answerId=intent.getIntExtra("answerId",0);
        retrofitGetSingleAnswer(answerId);

        layout_study_info_to_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer!=null) {
                    Intent intent = new Intent(StudyInfoToAnswer.this, QuestionActivity.class);
                    intent.putExtra("issue", answer.getIssue());
                    startActivity(intent);
                }
            }
        });
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyInfoToAnswer.this, PersonalInfoActivity.class);
                intent.putExtra("uid","s"+answer.getIssue().getUser().getId());
                startActivity(intent);
            }
        });
    }

    private void retrofitGetSingleAnswer(int id){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceStudent getSingleAnswer=retrofit.create(InterfaceStudent.class);
        final Call<Answer> call=getSingleAnswer.getSingleAnswer(id);
        call.enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                answer=response.body();
                Log.e("answerrrr",answer.toString());
                tvName.setText( answer.getIssue().getUser().getNickname());
                tvTime.setText(TimeUtils.Format(answer.getTime()) );
                tvContent.setText(answer.getContent());
                tvTitle.setText(answer.getIssue().getTitle());
                tvAgree.setText(answer.getAgree()+"人赞同");
            }
            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                alertDialog.showAlertDialgo(StudyInfoToAnswer.this,t.toString());
            }
        });
    }
}
