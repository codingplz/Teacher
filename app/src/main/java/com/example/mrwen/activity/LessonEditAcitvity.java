package com.example.mrwen.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceLesson;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.otherclass.FileRequestBody;
import com.example.mrwen.otherclass.RetrofitCallbcak;
import com.example.mrwen.staticClass.StaticInfo;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonEditAcitvity extends AppCompatActivity {

    @Bind(R.id.et_lesson_edit_lessonName)
    EditText et_lesson_edit_lessonName;
    @Bind(R.id.et_lesson_konwledgePoint)
    EditText et_lesson_konwledgePoint;
    @Bind(R.id.tv_lesson_description)
    TextView tv_lesson_description;
    @Bind(R.id.tv_upload_video)
    TextView tv_upload_video;


    String lessonName;
    String knowledgePoint;
    String description;
    String chapterId;
    int isSigned;
    private ArrayAdapter<Integer> lessonNumberAdapter;

    private ProgressDialog progressDialog;
    String video_path;
    String video_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_edit);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        isSigned=intent.getIntExtra("isSigned",0);

        //点击编辑
        tv_lesson_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myEditDialog=new MyDialog();
                myEditDialog.showMultiLineInputDialog(LessonEditAcitvity.this,"提示",null, tv_lesson_description);
            }
        });

        //点击上传
        tv_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent innerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                innerIntent.setType("video/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent,null);
                startActivityForResult(wrapperIntent,1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                    cursor.moveToFirst();
                    video_path = cursor.getString(1);
                    video_size = cursor.getString(3);
                    tv_upload_video.setText("已选择");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showProcessDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("上传视频");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();

    }

    //从控件获取信息
    private String getInfo(){
        lessonName=et_lesson_edit_lessonName.getText().toString();
        knowledgePoint=et_lesson_konwledgePoint.getText().toString();
        String temp=tv_lesson_description.getText().toString();
        if(temp.equals("点击编辑"))
            description="";
        else{
            description=temp;
        }
        String string=lessonName+"_"+knowledgePoint+"_"+description+"_"+StaticInfo.currentChapterId;
        Log.i("getInfogetInfo",string);
        return string;
    }

    //actionBar显示创建课程的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_save, menu);
        return true;
    }
    //保存个人信息menu设置按钮响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_save_item) {
            if(video_path!=null)
                retrofitVideoUpload();
            else{
                final MyDialog alertDialog=new MyDialog();
                alertDialog.showAlertDialgo(LessonEditAcitvity.this,"请选择教学视频");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrofitVideoUpload(){
        showProcessDialog();
        RetrofitCallbcak<UniversalResult> callbcak = new RetrofitCallbcak<UniversalResult>() {
            @Override
            public void onSuccess(Call<UniversalResult> call, Response<UniversalResult> response) {
                final MyDialog alertDialog=new MyDialog();
                progressDialog.dismiss();
                if(response.body().getResultCode()==1){
                    retrofitLessonNumberPlus();
                    if(isSigned==0){
                        retrofitChapterNumberPlus();
                    }
                    new AlertDialog.Builder(LessonEditAcitvity.this).setTitle("提示")
                            .setMessage("课程上传成功")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLessonCatalog=new Intent(LessonEditAcitvity.this,LessonCatalogActivity.class);
                                    startActivity(intentLessonCatalog);
                                    finish();
                                }
                            })
                            .show();
                }else{
                    new AlertDialog.Builder(LessonEditAcitvity.this).setTitle("提示")
                            .setMessage("课程上传失败")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLessonCatalog=new Intent(LessonEditAcitvity.this,LessonCatalogActivity.class);
                                    startActivity(intentLessonCatalog);
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                progressDialog.dismiss();
                final MyDialog alertDialog=new MyDialog();
                alertDialog.showAlertDialgo(LessonEditAcitvity.this,t.toString());
            }
            @Override
            public void onLoading(final long total, final long process) {
                super.onLoading(total, process);
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        int currentProcess = (int) (process*1.0f/Integer.parseInt(video_size)*100.0f);
                        Log.i("current progress total",currentProcess+" "+total+" "+process);
                        progressDialog.setProgress(currentProcess);
                    }
                });
            }
        };
        String lessonInfo=getInfo();
        File file = new File(video_path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"),file);

        FileRequestBody<UniversalResult> body = new FileRequestBody(requestBody,callbcak);
        MultipartBody.Part part = MultipartBody.Part.createFormData(file.getName(),file.getName(),body);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceLesson addVideoServes = retrofit.create(InterfaceLesson.class);
        Call<UniversalResult> call = addVideoServes.lessonUpload(lessonInfo,part);
        call.enqueue(callbcak);
    }

    private void retrofitLessonNumberPlus(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceChapter LessonNumberPlus=retrofit.create(InterfaceChapter.class);
        final Call<UniversalResult> call=LessonNumberPlus.LessonNumberPlus(Integer.parseInt(StaticInfo.currentChapterId));
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(LessonEditAcitvity.this,t.toString());
            }
        });
    }


    private void retrofitChapterNumberPlus(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse chapterNumberPlus=retrofit.create(InterfaceCourse.class);
        final Call<UniversalResult> call=chapterNumberPlus.chapterNumberPlus(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(LessonEditAcitvity.this,t.toString());
            }
        });
    }
}
