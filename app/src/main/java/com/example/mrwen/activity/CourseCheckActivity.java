package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/12/8.
 */

public class CourseCheckActivity extends AppCompatActivity{

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    private String name;
    private String grade;
    private String subject;
    private String description;

    @Bind(R.id.tv_course_check_courseName)
    TextView tv_course_check_courseName;
    @Bind(R.id.iv_course_check_cover)
    ImageView iv_course_check_cover;
    @Bind(R.id.tv_course_check_subject)
    TextView tv_course_check_subject;
    @Bind(R.id.tv_course_check_grade)
    TextView tv_course_check_grade;
    @Bind(R.id.tv_course_check_description)
    TextView tv_course_check_description;
    @Bind(R.id.layout_course_statistics)
    RelativeLayout layout_course_statistics;
    @Bind(R.id.tv_course_check_chapterNumber)
    TextView tv_course_check_chapterNumber;
    @Bind(R.id.layout_chapterCatalog)
    RelativeLayout layout_chapterCatalog;
    @Bind(R.id.layout_course_rank)
    RelativeLayout layout_course_rank;


    private Bitmap photo;
    int imageChange=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_information_check);
        ButterKnife.bind(this);
        retrofitGetSingleCourseInfo();


        layout_chapterCatalog=(RelativeLayout)findViewById(R.id.layout_chapterCatalog);

        layout_chapterCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChapterEdit=new Intent(CourseCheckActivity.this,ChapterCatalogActivity.class);
                startActivity(intentChapterEdit);
            }
        });


        final MyDialog myDialog=new MyDialog();
        //设置头像
        iv_course_check_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        //修改课程名称
        tv_course_check_courseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(CourseCheckActivity.this,"修改课程名称",tv_course_check_courseName.getText().toString(),tv_course_check_courseName);
            }
        });

        //修改课程科目
        tv_course_check_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(CourseCheckActivity.this,"修改课程科目",tv_course_check_subject.getText().toString(),tv_course_check_subject);
            }
        });

        //修改适用年级
        tv_course_check_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(CourseCheckActivity.this,"修改适用年级",tv_course_check_grade.getText().toString(),tv_course_check_grade);
            }
        });

        //修改课程介绍
        tv_course_check_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(CourseCheckActivity.this,"修改课程介绍",tv_course_check_description.getText().toString(),tv_course_check_description);
            }
        });

        //查看课程评价
        layout_course_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCourseRank=new Intent(CourseCheckActivity.this,RanksActivity.class);
                startActivity(intentCourseRank);
            }
        });
    }


    //获取信息
    private Map<String,String> getInfo(){
        name=tv_course_check_courseName.getText().toString();
        grade=tv_course_check_grade.getText().toString();
        subject=tv_course_check_subject.getText().toString();
        description=tv_course_check_description.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("grade",grade);
        map.put("subject",subject);
        map.put("description",description);
        map.put("courseId",String.valueOf(StaticInfo.currentCourseId));
        return map;
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void retrofitCoverUpload(){
        if(imageChange==1) {
            //requestBody = RequestBody.create(MediaType.parse("image/*"), imagePath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //iv_photo.setDrawingCacheEnabled(true);
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            RequestBody body = RequestBody.create(MediaType.parse("*.jpg"), outputStream.toByteArray());

            final MyDialog alertDialog=new MyDialog();
            Retrofit retrofitImageUpload=new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InterfaceCourse coverUpload=retrofitImageUpload.create(InterfaceCourse.class);

            final Call<UniversalResult> call=coverUpload.coverUpload(String.valueOf(StaticInfo.currentCourseId),body);
            call.enqueue(new Callback<UniversalResult>() {
                @Override
                public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                    if(response.body().getResultCode()==1){

                    }else {
                        alertDialog.showAlertDialgo(CourseCheckActivity.this,"上传失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(CourseCheckActivity.this,t.toString());
                }
            });
        }
        else
            ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);Log.i("onActivityResultttt","111");
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        imageChange=1;
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            //photo = PictureCut.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_course_check_cover.setImageBitmap(photo);
        }
    }

    //retrofit获得课程信息
    private void retrofitGetSingleCourseInfo(){
        Retrofit retrofitGetSingleCourseInfo=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MyDialog alertDialog=new MyDialog();
        final InterfaceCourse getSingleCourseInfo=retrofitGetSingleCourseInfo.create(InterfaceCourse.class);
        final Call<Course> call=getSingleCourseInfo.getSingleCourseInfo(Integer.parseInt(StaticInfo.currentCourseId));
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Course course=response.body();
                StaticInfo.currentCourseId=String.valueOf(course.getId());
                tv_course_check_courseName.setText(course.getName());
                tv_course_check_subject.setText(course.getSubject().getTitle());
                tv_course_check_grade.setText(course.getGrade());
                tv_course_check_description.setText(course.getDescription());
                tv_course_check_chapterNumber.setText(course.getChapterNumber());
                if(course.getCoverURL()!=null)
                    Glide.with(CourseCheckActivity.this).load(getResources().getString(R.string.baseURL) + course.getCoverURL()).into(iv_course_check_cover);
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                alertDialog.showAlertDialgo(CourseCheckActivity.this,t.toString());
            }
        });
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
            retrofitCoverUpload();
            retofitCourseInfoRevise();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retofitCourseInfoRevise(){
        Map<String,String> map=getInfo();
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofitLoginIn=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceCourse courseInfoRevise=retrofitLoginIn.create(InterfaceCourse.class);
        final Call<UniversalResult> call=courseInfoRevise.courseInfoRevise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                final UniversalResult loginInResult=response.body();

                int resultCode=loginInResult.getResultCode();

                if(resultCode==1){

                    new AlertDialog.Builder(CourseCheckActivity.this).setTitle("提示")
                            .setMessage("保存课程信息成功")
                            .setPositiveButton("确认",null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(CourseCheckActivity.this,t.toString());
            }
        });
    }
}
