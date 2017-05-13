package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.mrwen.Utils.ActivityManager;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceQuestion;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WriteNoticeActivity extends AppCompatActivity {


    @Bind(R.id.et_write_notice_content)
    EditText et_write_notice_content;
    @Bind(R.id.et_write_notice_name)
    EditText et_write_notice_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);
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

    //提交公告
    private void submit() {
        final MyDialog alertDialog = new MyDialog();
        String name = et_write_notice_name.getText().toString();
        String content = et_write_notice_content.getText().toString();
        if (name.isEmpty() || content.isEmpty()) {
            alertDialog.showAlertDialgo(this, "公告标题或者内容不能为空");
        } else {
            Map<String,String> map=new HashMap<>();
            map.put("courseId",StaticInfo.currentCourseId);
            map.put("name",name);
            map.put("content",content);
            map.put("type","0");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final InterfaceCourse noticeUpload = retrofit.create(InterfaceCourse.class);
            final Call<UniversalResult> call = noticeUpload.noticeUpload(map);
            call.enqueue(new Callback<UniversalResult>() {
                @Override
                public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                    if (response.body().getResultCode() == 1) {
                        new AlertDialog.Builder(WriteNoticeActivity.this).setTitle("提示")
                                .setMessage("上传公告成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(WriteNoticeActivity.this, CourseNoticeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        ActivityManager.destoryActivity("CourseNoticeActivity");
                                    }
                                })
                                .show();
                    } else {
                        new AlertDialog.Builder(WriteNoticeActivity.this).setTitle("提示")
                                .setMessage("上传公告失败")
                                .setPositiveButton("确认", null)
                                .show();
                    }
                }
                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(WriteNoticeActivity.this, t.toString());
                }
            });
        }
    }

}
