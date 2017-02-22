package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Result;
import com.example.mrwen.interfaces.InterfaceQuestion;
import com.example.mrwen.otherclass.Question;
import com.example.mrwen.staticClass.StaticInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WriteAnswerActivity extends AppCompatActivity {

    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.switch_anonymous)
    Switch switchAnonymous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_answer_submit) {
            submit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        boolean anonymous = switchAnonymous.isChecked();
        int iid = getIntent().getIntExtra("iid", -1);
        String content = etContent.getText().toString();
        if (!content.trim().isEmpty()) {

            final MyDialog alertDialog=new MyDialog();
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final InterfaceQuestion submitAnswer=retrofit.create(InterfaceQuestion.class);
            final Call<Result> call=submitAnswer.submitAnswer(content, anonymous, iid, StaticInfo.uid);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if(response.body().getCode()==0) {
                        new AlertDialog.Builder(WriteAnswerActivity.this).setTitle("提示")
                                .setMessage("回答问题成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(WriteAnswerActivity.this, QuestionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                    else{
                        new AlertDialog.Builder(WriteAnswerActivity.this).setTitle("提示")
                                .setMessage("回答失败")
                                .setPositiveButton("确认", null)
                                .show();
                    }
                }
                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    alertDialog.showAlertDialgo(WriteAnswerActivity.this,t.toString());
                }
            });
        } else {
            new AlertDialog.Builder(this).setMessage("请输入回答内容").setPositiveButton("确定", null).show();
        }

    }
}
