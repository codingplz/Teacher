package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class PassReviseActivity extends AppCompatActivity {

    @Bind(R.id.bt_old_pass_clear)
    Button bt_old_pass_clear;
    @Bind(R.id.bt_new_pass_clear)
    Button bt_new_pass_clear;
    @Bind(R.id.bt_new_pass_again_clear)
    Button bt_new_pass_again_clear;
    @Bind(R.id.bt_pass_revise)
    Button bt_pass_revise;
    @Bind(R.id.et_old_pass)
    EditText et_old_pass;
    @Bind(R.id.et_new_pass)
    EditText et_new_pass;
    @Bind(R.id.et_new_pass_again)
    EditText et_new_pass_again;

    String pass;
    String newPass;
    String newPassAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_revise);
        ButterKnife.bind(this);

        bt_old_pass_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_old_pass.setText("");
            }
        });
        bt_new_pass_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_new_pass.setText("");
            }
        });
        bt_new_pass_again_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_new_pass_again.setText("");
            }
        });
        bt_pass_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=et_old_pass.getText().toString();
                newPass=et_new_pass.getText().toString();
                newPassAgain=et_new_pass_again.getText().toString();
                if(!pass.equals(StaticInfo.password)){
                    new AlertDialog.Builder(PassReviseActivity.this).setMessage("旧密码不相符").show();
                }else if(newPass.length()<6||newPassAgain.length()<6){
                    new AlertDialog.Builder(PassReviseActivity.this).setMessage("请输入6位以上的新密码").show();
                }else if(!newPass.equals(newPassAgain)){
                    new AlertDialog.Builder(PassReviseActivity.this).setMessage("两次输入的新密码不一致").show();
                }else {
                    retrofitPassRevise();
                }
            }
        });
    }

    private void retrofitPassRevise(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher passwordRevise=retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call=passwordRevise.passwordRevise(StaticInfo.uid,newPass);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if (response.body().getResultCode() == 1) {
                    new AlertDialog.Builder(PassReviseActivity.this).setTitle("提示")
                            .setMessage("修改密码成功")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(PassReviseActivity.this,LoginInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }
                else{
                    new AlertDialog.Builder(PassReviseActivity.this).setTitle("提示")
                            .setMessage("修改密码失败")
                            .setPositiveButton("确认",null)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                alertDialog.showAlertDialgo(PassReviseActivity.this,t.toString());
            }
        });

    }
}
