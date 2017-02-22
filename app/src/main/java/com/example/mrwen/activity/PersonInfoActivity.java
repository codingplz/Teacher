package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.StringSignature;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.PictureCut;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/11/1.
 */

public class PersonInfoActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    @Bind(R.id.iv_person_info_photo)
    ImageView iv_person_info_photo;
    @Bind(R.id.tv_person_info_name)
    TextView tv_person_info_name;
    @Bind(R.id.tv_person_info_gender)
    TextView tv_person_info_gender;
    @Bind(R.id.tv_person_info_rank)
    TextView tv_person_info_rank;
    @Bind(R.id.tv_person_info_subject)
    TextView tv_person_info_subject;
    @Bind(R.id.tv_person_info_province)
    TextView tv_person_info_province;
    @Bind(R.id.tv_person_info_city)
    TextView tv_person_info_city;
    @Bind(R.id.tv_person_info_signature)
    TextView tv_person_info_signature;
    @Bind(R.id.tv_person_info_phone)
    TextView tv_person_info_phone;
    @Bind(R.id.tv_person_info_email)
    TextView tv_person_info_email;
    @Bind(R.id.r_layout_region)
    RelativeLayout r_layout_region;

    private Bitmap photo;
    int imageChange=0;

    private String realname;
    private String gender;
    private String province;
    private String city;
    private String rank;
    private String subject;
    private String email;
    private String signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_information_main);
        ButterKnife.bind(this);
        tv_person_info_name.setText(StaticInfo.realname);
        tv_person_info_gender.setText(StaticInfo.gender);
        tv_person_info_rank.setText(StaticInfo.rank);
        tv_person_info_subject.setText(StaticInfo.subject);
        tv_person_info_province.setText(StaticInfo.province);
        tv_person_info_city.setText(StaticInfo.city);
        tv_person_info_phone.setText(StaticInfo.phone);
        tv_person_info_email.setText(StaticInfo.email);
        tv_person_info_signature.setText(StaticInfo.signature);
        if(StaticInfo.imageURL!=null) {
            Glide.with(this).load(getResources().getString(R.string.baseURL) + StaticInfo.imageURL).signature(new StringSignature(UUID.randomUUID().toString())).into(iv_person_info_photo);
        }
        final MyDialog myDialog=new MyDialog();
        //设置头像
        iv_person_info_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        //修改姓名
        tv_person_info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(PersonInfoActivity.this,"修改姓名",tv_person_info_name.getText().toString(),tv_person_info_name);
            }
        });

        //修改性别
        tv_person_info_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showGenderSelectDialog(PersonInfoActivity.this,"修改性别",tv_person_info_gender.getText().toString(),tv_person_info_gender);
            }
        });

        //修改年级
        tv_person_info_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(PersonInfoActivity.this,"修改年级",tv_person_info_rank.getText().toString(),tv_person_info_rank);
            }
        });

        //修改科目
        tv_person_info_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(PersonInfoActivity.this,"修改科目",tv_person_info_subject.getText().toString(),tv_person_info_subject);
            }
        });

        //修改地区
        r_layout_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showRegionReviseDialog(PersonInfoActivity.this,"修改地区",tv_person_info_province.getText().toString(),tv_person_info_city.getText().toString(),tv_person_info_province,tv_person_info_city);
            }
        });

        //修改签名
        tv_person_info_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showMultiLineInputDialog(PersonInfoActivity.this,"修改签名",tv_person_info_signature.getText().toString(),tv_person_info_signature);
            }
        });

        //修改邮箱
        tv_person_info_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.showSingleLineInputDialog(PersonInfoActivity.this,"修改邮箱",tv_person_info_email.getText().toString(),tv_person_info_email);
            }
        });


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
            photo = PictureCut.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            Log.i("photoo",photo.toString());
            iv_person_info_photo.setImageBitmap(photo);
        }
    }

    private void retrofitImageUpload(){
        if(imageChange==1) {
            //requestBody = RequestBody.create(MediaType.parse("image/*"), imagePath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //iv_photo.setDrawingCacheEnabled(true);
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            RequestBody body = RequestBody.create(MediaType.parse("*.png"), outputStream.toByteArray());

            final MyDialog alertDialog=new MyDialog();
            Retrofit retrofitImageUpload=new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InterfaceTeacher imageUpload=retrofitImageUpload.create(InterfaceTeacher.class);

            final Call<UniversalResult> call=imageUpload.imageUpload(String.valueOf(StaticInfo.id),body);
            call.enqueue(new Callback<UniversalResult>() {
                @Override
                public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                    if(response.body().getResultCode()==1){
                        StaticInfo.imageURL="image/"+StaticInfo.id+".png";
                    }else {
                        alertDialog.showAlertDialgo(PersonInfoActivity.this,"上传头像失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(PersonInfoActivity.this,t.toString());
                }
            });
        }
        else
            ;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info_save_item) {
            retrofitImageUpload();
            retofitPersonInfoRevise();
            setResult(1);

        }
        return super.onOptionsItemSelected(item);
    }
    //获取信息
    private Map<String,String> getInfo(){
        realname=tv_person_info_name.getText().toString();
        gender=tv_person_info_gender.getText().toString();
        province=tv_person_info_province.getText().toString();
        city=tv_person_info_city.getText().toString();
        rank=tv_person_info_rank.getText().toString();
        subject=tv_person_info_subject.getText().toString();
        email=tv_person_info_email.getText().toString();
        signature=tv_person_info_signature.getText().toString();

        Map<String,String> map = new HashMap<>();
        map.put("realname",realname);
        map.put("gender",gender);
        map.put("province",province);
        map.put("city",city);
        map.put("rank",rank);
        map.put("subject",subject);
        map.put("email",email);
        map.put("signature",signature);
        map.put("teacherId",String.valueOf(StaticInfo.id));
        return map;
    }

    private void retofitPersonInfoRevise(){
        Map<String,String> map=getInfo();
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofitLoginIn=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher teacherInfoRevise=retrofitLoginIn.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=teacherInfoRevise.teacherInfoRevise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                final UniversalResult loginInResult=response.body();

                int resultCode=loginInResult.getResultCode();

                if(resultCode==1){

                        new AlertDialog.Builder(PersonInfoActivity.this).setTitle("提示")
                                .setMessage("保存个人信息成功")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StaticInfo.realname=realname;
                                        StaticInfo.gender=gender;
                                        StaticInfo.province=province;
                                        StaticInfo.city=city;
                                        StaticInfo.rank=rank;
                                        StaticInfo.subject=subject;
                                        StaticInfo.email=email;
                                        StaticInfo.signature=signature;
                                        RongIM instance = RongIM.getInstance();
                                        instance.refreshUserInfoCache(new io.rong.imlib.model.UserInfo(StaticInfo.uid,StaticInfo.realname, Uri.parse(getResources().getString(R.string.baseURL)+StaticInfo.imageURL)));
                                    }
                                })
                                .show();
                }
            }

            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(PersonInfoActivity.this,t.toString());
            }
        });
    }


}
