package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceChapter;
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

public class ChapterEditAcitvity extends AppCompatActivity {

    @Bind(R.id.et_chapter_edit_unitName)
    EditText et_chapter_edit_unitName;
    @Bind(R.id.et_chapter_konwledgePoint)
    EditText et_chapter_konwledgePoint;
    @Bind(R.id.tv_chapter_description)
    TextView tv_chapter_description;
    @Bind(R.id.bt_saveUnit)
    Button bt_saveUnit;

    String chapterName;
    String knowledgePoint;
    String description;
    String courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_edit);
        ButterKnife.bind(this);

        //点击编辑
        tv_chapter_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myEditDialog=new MyDialog();
                myEditDialog.showMultiLineInputDialog(ChapterEditAcitvity.this,"提示",null, tv_chapter_description);
            }
        });

        //点击保存单元
        bt_saveUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitChapterUpload();
            }
        });
    }
    //从控件获取信息
    private Map<String,String> getInfo(){
        chapterName=et_chapter_edit_unitName.getText().toString();
        knowledgePoint=et_chapter_konwledgePoint.getText().toString();
        description=tv_chapter_description.getText().toString();
        courseId= StaticInfo.currentCourseId;

        Map<String,String > map=new HashMap<>();
        map.put("chapterName",chapterName);
        map.put("knowledgePoint",knowledgePoint);
        map.put("description",description);
        map.put("courseId",courseId);
        return map;
    }

    //retrofit存单元信息
    private void retrofitChapterUpload(){
        {
            final MyDialog alertDialog=new MyDialog();
            Map<String, String> map = getInfo();
            Retrofit retrofitChapterUpload=new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InterfaceChapter chapterUpload=retrofitChapterUpload.create(InterfaceChapter.class);

            final Call<UniversalResult> call=chapterUpload.chapterUpload(map);
            call.enqueue(new Callback<UniversalResult>() {
                @Override
                public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                    if(response.body().getResultCode()==1){
                        new AlertDialog.Builder(ChapterEditAcitvity.this).setTitle("提示")
                                .setMessage("创建章节成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(ChapterEditAcitvity.this,ChapterListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }else {
                        alertDialog.showAlertDialgo(ChapterEditAcitvity.this,"创建章节失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(ChapterEditAcitvity.this,t.toString());
                }
            });

        }
    }


}
