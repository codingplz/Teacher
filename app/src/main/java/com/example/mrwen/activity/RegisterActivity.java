package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrwen on 2016/12/7.
 */

public class RegisterActivity extends AppCompatActivity{

    @Bind(R.id.bt_register)
    Button register;
    @Bind(R.id.bt_username_clear)
    Button usernameClear;
    @Bind(R.id.bt_pwd_clear)
    Button passwordClear;
    @Bind(R.id.username)
    EditText et_username;
    @Bind(R.id.password)
    EditText et_password;
    @Bind(R.id.et_register_password_again)
    EditText et_register_password_again;
    @Bind(R.id.bt_pwd_again_clear)
    Button bt_pwd_again_clear;

    String username;
    String password;
    String password_again;

    Boolean isUserNameUnique=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ButterKnife.bind(this);

        //清空用户名按钮
        usernameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setText("");
            }
        });

        //清空密码按钮
        passwordClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setText("");
            }
        });

        bt_pwd_again_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_register_password_again.setText("");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=et_username.getText().toString();
                password=et_password.getText().toString();
                password_again=et_register_password_again.getText().toString();
                MyDialog alertDialog=new MyDialog();
                if(username.length()<8||username.length()>11)
                    alertDialog.showAlertDialgo(RegisterActivity.this,"请输入8-11位的用户名");
                else if(password.length()<6)
                    alertDialog.showAlertDialgo(RegisterActivity.this,"请输入6位以上的密码");
                else if(!isUserNameUnique){
                    alertDialog.showAlertDialgo(RegisterActivity.this,"该用户名已被注册");
                }else if(password.equals(password_again)){
                    Intent intentPersonalInfoFill=new Intent(RegisterActivity.this,PersonInfoFillActivity.class);
                    intentPersonalInfoFill.putExtra("username",username);
                    intentPersonalInfoFill.putExtra("password",password);
                    startActivity(intentPersonalInfoFill);
                    finish();
                }else {
                    alertDialog.showAlertDialgo(RegisterActivity.this,"两次输入的密码不一致");
                }
            }
        });

    }
    private void retrofitIsUsernameUnique(String username){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher isUsernameUnique=retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=isUsernameUnique.isUsernameUnique(username);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    isUserNameUnique=true;
                }
                else{
                    isUserNameUnique=false;
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(RegisterActivity.this,t.toString());
            }
        });
    }
}
