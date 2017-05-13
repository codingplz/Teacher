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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mrwen.Utils.PictureCut;
import com.example.mrwen.bean.RegisterResult;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.otherclass.RegionDAO;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonInfoFillActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    //对象的变量
    private  int id;
    private String region;
    private String username;
    private String password;
    private String realname;
    private String gender;
    private String rank;
    private String subject;
    private String phone;
    private String email;
    private String signature;

    private Bitmap photo;
    int imageChange=0;

    //获取信息的控件
    private EditText et_realname;
    private Spinner sp_gender;
    private EditText et_rank;
    private EditText et_subject;
    private EditText et_phone;
    private EditText et_email;
    private TextView tv_dialog;
    private ImageView iv_photo;
    private Spinner spinnerProvince;
    private Spinner spinnerCity;
    private Spinner spinnerArea;

    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mAreaAdapter;
    private List<Map<String, String>> mProvinces;
    private List<Map<String, String>> mCities;

    private Button bt_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_infomation_edit);
        Intent lastIntent=getIntent();
        username=lastIntent.getStringExtra("username");
        password=lastIntent.getStringExtra("password");

        et_realname=(EditText)findViewById(R.id.et_realname);
        sp_gender=(Spinner)findViewById(R.id.sp_gender);
        et_rank=(EditText)findViewById(R.id.et_rank);
        et_subject=(EditText)findViewById(R.id.et_subject);
        et_phone=(EditText) findViewById(R.id.et_phone);
        et_email=(EditText) findViewById(R.id.et_email);
        tv_dialog=(TextView)findViewById(R.id.tv_signature);
        bt_save=(Button)findViewById(R.id.bt_personInfoEditSave);
        iv_photo=(ImageView)findViewById(R.id.iv_photo);
        spinnerProvince=(Spinner)findViewById(R.id.sp_person_info_fill_province);
        spinnerCity=(Spinner)findViewById(R.id.sp_person_info_fill_city);
        spinnerArea=(Spinner)findViewById(R.id.sp_person_info_fill_area);
        initView();

        //点击编辑
        tv_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myEditDialog=new MyDialog();
                myEditDialog.showMultiLineInputDialog(PersonInfoFillActivity.this,"提示",null, tv_dialog);
            }
        });

        //头像按钮
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        //保存按钮
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitInfoUpload();
            }
        });
    }
    //上传头像的retrofit请求
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

                    }else {
                        alertDialog.showAlertDialgo(PersonInfoFillActivity.this,"上传头像失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialgo(PersonInfoFillActivity.this,t.toString());
                }
            });
        }
        else
            ;
    }


    //上传信息的retrofit请求
    private void retrofitInfoUpload(){
        final MyDialog alertDialog=new MyDialog();
        Map<String, String> map = getInformation();
        Retrofit retrofitRegister=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceTeacher register=retrofitRegister.create(InterfaceTeacher.class);

        final Call<RegisterResult> call=register.register(map);
        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if(response.body().getResultCode()==1){
                    StaticInfo.id=response.body().getId();
                    retrofitImageUpload();
                    new AlertDialog.Builder(PersonInfoFillActivity.this).setTitle("提示")
                            .setMessage("注册成功")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intentLoginIn=new Intent(PersonInfoFillActivity.this,LoginInActivity.class);
                                    startActivity(intentLoginIn);
                                    finish();
                                }
                            })
                            .show();
                }else {
                    alertDialog.showAlertDialgo(PersonInfoFillActivity.this,"注册失败");
                }

            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                alertDialog.showAlertDialgo(PersonInfoFillActivity.this,t.toString());
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
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        imageChange=1;
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
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

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            photo = PictureCut.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_photo.setImageBitmap(photo);
            iv_photo.setVisibility(View.VISIBLE);
            //uploaidPic(photo);
        }
    }


    //信息的提取赋值
    private Map<String,String> getInformation(){
        realname=et_realname.getText().toString();
        gender=sp_gender.getSelectedItem().toString();
        region=spinnerProvince.getSelectedItem().toString()+"_"+spinnerCity.getSelectedItem().toString()+"_"+spinnerArea.getSelectedItem().toString();
        rank=et_rank.getText().toString();
        subject=et_subject.getText().toString();
        phone=et_phone.getText().toString();
        email=et_email.getText().toString();
        signature=tv_dialog.getText().toString();
        if(signature=="点击编辑")
            signature="";
        //image=((BitmapDrawable) ((ImageView) iv_photo).getDrawable()).getBitmap();
        //Log.i("image",image.toString());
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        map.put("realname",realname);
        map.put("gender",gender);
        map.put("region",region);
        map.put("rank",rank);
        map.put("subject",subject);
        map.put("phone",phone);
        map.put("email",email);
        map.put("signature",signature);
        return map;
    }

    private void initView() {
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerProvince.setAdapter(provinceAdapter);
        mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerCity.setAdapter(mCityAdapter);
        mAreaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        spinnerArea.setAdapter(mAreaAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int number = refreshCityByProvince(position);
                if (number != 0) {
                    int selectedItemPosition = spinnerCity.getSelectedItemPosition();
                    spinnerCity.setSelection(0);
                    spinnerArea.setSelection(0);
                    if (selectedItemPosition == 0)
                        refreshAreaByCity(0);
                } else {
                    mAreaAdapter.clear();
                    mAreaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshAreaByCity(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mProvinces = RegionDAO.getProvinces();
        provinceAdapter.clear();
        provinceAdapter.addAll(flatList(mProvinces));
        provinceAdapter.notifyDataSetChanged();
    }
    private void refreshAreaByCity(int position) {

        Map<String, String> map = mCities.get(position);
        String cityID = map.get("id");
        List<String> areas = RegionDAO.getAreaByCity(cityID);
        mAreaAdapter.clear();
        if (areas.size() != 0)
            mAreaAdapter.addAll(areas);
        mAreaAdapter.notifyDataSetChanged();

    }

    private int refreshCityByProvince(int position) {

        Map<String, String> map = mProvinces.get(position);
        mCities = RegionDAO.getCityByProvince(map.get("id"));
        mCityAdapter.clear();
        if (mCities.size() != 0)
            mCityAdapter.addAll(flatList(mCities));
        mCityAdapter.notifyDataSetChanged();
        return mCities.size();
    }

    private List<String> flatList(List<Map<String, String>> list) {
        List<String> names = new ArrayList<>();
        for (Map<String, String> item : list) {
            names.add(item.get("name"));
        }
        return names;
    }
}
