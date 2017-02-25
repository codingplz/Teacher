package com.example.mrwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.LoginInResult;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.Teacher;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/11/26.
 */

public class LoginInActivity extends AppCompatActivity {
    private Button bt_loginIn;
    private Button bt_forgetPass;
    private Button bt_register;
    private EditText et_username;
    private EditText et_password;

    @Bind(R.id.bt_login_pwd_remove)
    Button bt_login_pwd_remove;
    @Bind(R.id.bt_login_username_clear)
    Button bt_login_username_clear;
    @Bind(R.id.bt_pwd_visible)
    Button bt_pwd_visible;

    private String username;
    private String password;

    MyDialog alertDialog=new MyDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife. bind(this);
        bt_loginIn=(Button)findViewById(R.id.bt_login);
        bt_register=(Button)findViewById(R.id.bt_register);
        bt_forgetPass=(Button)findViewById(R.id.bt_forgetPass);
        et_username=(EditText)findViewById(R.id.username);
        et_password=(EditText)findViewById(R.id.password);

        //清空用户名
        bt_login_username_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setText("");
            }
        });

        //清空密码
        bt_login_pwd_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setText("");
            }
        });

        //密码可见
        bt_pwd_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setTransformationMethod(null);

            }
        });

        //注册按钮响应
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister=new Intent(LoginInActivity.this,RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        //登录按钮响应
        bt_loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map=getUserNamePassword();
                Retrofit retrofitLoginIn=new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.baseURL))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final InterfaceTeacher loginIn=retrofitLoginIn.create(InterfaceTeacher.class);
                final Call<LoginInResult> call=loginIn.loginIn(map);
                call.enqueue(new Callback<LoginInResult>() {
                    @Override
                    public void onResponse(Call<LoginInResult> call, Response<LoginInResult> response) {
                        final LoginInResult loginInResult=response.body();

                        int resultCode=loginInResult.getResultCode();
                        StaticInfo.token=response.body().getTeacher().getToken();
                        switch (resultCode){
                            case 0:
                                alertDialog.showAlertDialgo(LoginInActivity.this,"用户名不存在");
                                break;
                            case 1:
                                alertDialog.showAlertDialgo(LoginInActivity.this,"用户存在异常");
                                break;
                            case 2:
                                alertDialog.showAlertDialgo(LoginInActivity.this,"密码错误");
                                break;
                            default:
                                Toast.makeText(LoginInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                loginInSucceed(loginInResult);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginInResult> call, Throwable t) {
                        alertDialog.showAlertDialgo(LoginInActivity.this,t.toString());
                    }
                });
            }
        });


    }

    //获得用户名密码
    private Map<String,String> getUserNamePassword(){
        Map<String,String> map=new HashMap<>();
        username=et_username.getText().toString();
        password=et_password.getText().toString();


        map.put("username",username);
        map.put("password",password);
        return  map;
    }



    //登陆成功
    private void loginInSucceed(LoginInResult loginInResult){
        Teacher teacher=loginInResult.getTeacher();
        StaticInfo.id=teacher.getId();
        StaticInfo.identity=teacher.getIdentity();
        StaticInfo.username=teacher.getUsername();
        StaticInfo.password=teacher.getPassword();
        StaticInfo.realname=teacher.getRealname();
        StaticInfo.gender=teacher.getGender();
        StaticInfo.region=teacher.getRegion();
        StaticInfo.rank=teacher.getRank();
        StaticInfo.subject=teacher.getSubject();
        StaticInfo.phone=teacher.getPhone();
        StaticInfo.email=teacher.getEmail();
        StaticInfo.signature=teacher.getSignature();
        StaticInfo.imageURL=teacher.getImageURL();
        StaticInfo.uid="t"+teacher.getId();

        storeInfo(StaticInfo.username,StaticInfo.password);
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(String uid) {
                return findUserByUid(uid);
            }
        },true);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                Intent intent = new Intent(context, PersonalInfoActivity.class);
                intent.putExtra("uid",info.getUserId());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });


        RongIM.connect(StaticInfo.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                alertDialog.showAlertDialgo(LoginInActivity.this,"连接融云服务器失败");
            }

            @Override
            public void onSuccess(String s) {

                Intent intentMainActivity=new Intent(LoginInActivity.this,MainActivity.class);
                intentMainActivity.putExtra("token",StaticInfo.token);
                startActivity(intentMainActivity);
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                alertDialog.showAlertDialgo(LoginInActivity.this,"连接融云服务器失败");
            }
        });
    }

    private void storeInfo(String username, String password) {
        SharedPreferences.Editor info = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
        info.putString("username",username);
        info.putString("password",password);
        info.putBoolean("needlogin",false);
        info.commit();
    }


    private io.rong.imlib.model.UserInfo findUserByUid(String uid) {
        try {

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final InterfaceTeacher getUserInfo=retrofit.create(InterfaceTeacher.class);
            final Call<Info> call=getUserInfo.getUserInfo(uid,StaticInfo.uid);
            Response<Info> response = call.execute();
            Info info = response.body();
            io.rong.imlib.model.UserInfo userInfo=null;
            if (info!=null)
                userInfo = new io.rong.imlib.model.UserInfo(info.getUid(), info.getNickname(), Uri.parse(getResources().getString(R.string.baseURL) + info.getImageURL()));
            return userInfo;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

