package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.CourseResult;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
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

public class CourseEditActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    private Bitmap photo;
    int imageChange=0;

    @Bind(R.id.et_courseName)
    EditText et_courseName;
    @Bind(R.id.et_apply_grade)
    EditText et_apply_grade;
    @Bind(R.id.et_subject)
    EditText et_subject;
    @Bind(R.id.tv_description)
    TextView tv_description;
    @Bind(R.id.bt_saveCourse)
    Button bt_saveCourse;
    @Bind(R.id.iv_cover)
    ImageView iv_cover;
    @Bind(R.id.sp_semester)
    Spinner sp_semester;
    @Bind(R.id.sp_courseMajor)
    Spinner sp_courseMajor;

    String name;
    String subject;
    String grade;
    String description;
    String semester;
    String major;
    int teacherID;
    String[] semesterArray={"上学期","下学期"};
    String[] majorArray={"必修","辅修","选修"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        ArrayAdapter<String> semesterAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,semesterArray);
        ArrayAdapter<String> majorAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,majorArray);
        sp_semester.setAdapter(semesterAdapter);
        sp_courseMajor.setAdapter(majorAdapter);



        iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });


        //点击编辑
        tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myEditDialog=new MyDialog();
                myEditDialog.showMultiLineInputDialog(CourseEditActivity.this,"课程信息",null, tv_description);
            }
        });

        //保存按钮
        bt_saveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyDialog alertDialog=new MyDialog();
                Map<String, String> map = getInformation();
                Retrofit retrofitCourseUpload=new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.baseURL))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                InterfaceCourse courseUpload=retrofitCourseUpload.create(InterfaceCourse.class);

                final Call<CourseResult> call=courseUpload.courseUpload(map);
                call.enqueue(new Callback<CourseResult>() {
                    @Override
                    public void onResponse(Call<CourseResult> call, Response<CourseResult> response) {
                        if(response.body().getResultCode()==1){
                            StaticInfo.currentCourseId=String.valueOf(response.body().getId());
                            retrofitCoverUpload();
                            new AlertDialog.Builder(CourseEditActivity.this).setTitle("提示")
                                    .setMessage("创建课程成功")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intentMain=new Intent(CourseEditActivity.this,MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        }
                                    })
                                    .show();
                        }else {
                            alertDialog.showAlertDialgo(CourseEditActivity.this,"创建课程失败");
                        }
                    }
                    @Override
                    public void onFailure(Call<CourseResult> call, Throwable t) {
                        alertDialog.showAlertDialgo(CourseEditActivity.this,t.toString());
                    }
                });
            }
        });
    }

    private Map<String,String> getInformation(){
        name=et_courseName.getText().toString();
        subject=et_subject.getText().toString();
        grade=et_apply_grade.getText().toString();
        description=tv_description.getText().toString();
        semester=sp_semester.getSelectedItem().toString();
        major=sp_courseMajor.getSelectedItem().toString();
        teacherID= StaticInfo.id;
        String stringTeacherID=String.valueOf(teacherID);
        if(description.equals("点击编辑"))
            description="";
        Map<String,String > map=new HashMap<>();
        map.put("name",name);
        map.put("subject",subject);
        map.put("grade",grade);
        map.put("description",description);
        map.put("teacherID",stringTeacherID);
        map.put("semester",semester);
        map.put("major",major);
        return map;
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
                        alertDialog.showAlertDialgo(CourseEditActivity.this,"上传失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(CourseEditActivity.this,t.toString());
                }
            });
        }
        else
            ;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            iv_cover.setImageBitmap(photo);
        }
    }
}
