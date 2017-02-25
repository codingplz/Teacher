package com.example.mrwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.LoginInResult;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.Teacher;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    String PREFERENCE_USERINFO = "UserInfo";
    //判断是否需要手动登录
    String KEY_NEED_LOGIN = "needlogin";


    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    AlphaAnimation showLogo;
    TranslateAnimation showTitleLayout, showTitleView;
    private boolean animationEnd;
    private boolean requestEnd;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case 0:
                    intent = new Intent(SplashActivity.this, LoginInActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.splash_enter,R.anim.splash_exit);
                    break;
                case 1:
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.splash_enter,R.anim.splash_exit);
                    break;
            }
        }
    };
    private boolean needLogin;
    private SharedPreferences mPreferences;


    private void initDB() {
        InputStream stream = null;
        FileOutputStream outputStream = null;
        try {
            String fileName = "region.db";
            File file = new File(getFilesDir(), fileName);
            if (file.exists())
                return;
            stream = getAssets().open(fileName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) != -1)
                outputStream.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Toast.makeText(this, "拷贝完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initDB();
        mPreferences = getSharedPreferences(PREFERENCE_USERINFO, MODE_PRIVATE);
        needLogin = mPreferences.getBoolean(KEY_NEED_LOGIN, true);
        if (!needLogin)
            sendRequest();
        else requestEnd = true;
        startAnimation();
    }

    private void sendRequest() {

        final MyDialog alertDialog=new MyDialog();
        Map<String,String> map=new HashMap<>();
        map.put("username",mPreferences.getString("username", ""));
        map.put("password",mPreferences.getString("password", ""));
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
                if(StaticInfo.token==null){
                    Intent intentLoginIn=new Intent(SplashActivity.this,LoginInActivity.class);
                    startActivity(intentLoginIn);
                }

                StaticInfo.token=response.body().getTeacher().getToken();
                Teacher teacher=response.body().getTeacher();
                switch (resultCode){
                    case 0:
                        alertDialog.showAlertDialgo(SplashActivity.this,"用户名不存在");
                        break;
                    case 1:
                        alertDialog.showAlertDialgo(SplashActivity.this,"用户存在异常");
                        break;
                    case 2:
                        alertDialog.showAlertDialgo(SplashActivity.this,"密码错误");
                        break;
                    default:
                        StaticInfo.imageURL = teacher.getImageURL();
                        StaticInfo.realname = teacher.getRealname();
                        StaticInfo.id = teacher.getId();
                        StaticInfo.uid = "t" + teacher.getId();
                        StaticInfo.token = teacher.getToken();
                        StaticInfo.signature = teacher.getSignature();
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public io.rong.imlib.model.UserInfo getUserInfo(String uid) {
                                return findUserByUid(uid);
                            }
                        }, true);
                        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                            @Override
                            public boolean onUserPortraitClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                                Intent intent = new Intent(context, PersonalInfoActivity.class);
                                intent.putExtra("uid", info.getUserId());
                                startActivity(intent);
                                return false;
                            }

                            @Override
                            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType type, io.rong.imlib.model.UserInfo info) {
                                return false;
                            }

                            @Override
                            public boolean onMessageClick(Context context, View view, io.rong.imlib.model.Message message) {
                                return false;
                            }

                            @Override
                            public boolean onMessageLinkClick(Context context, String s) {
                                return false;
                            }

                            @Override
                            public boolean onMessageLongClick(Context context, View view, io.rong.imlib.model.Message message) {
                                return false;
                            }
                        });
                        connect();
                }
            }

            @Override
            public void onFailure(Call<LoginInResult> call, Throwable t) {
                requestEnd = true;
                handler.sendEmptyMessageDelayed(0,500);
            }
        });
    }

    public void connect() {

        RongIM.connect(StaticInfo.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                requestForToken();
            }

            @Override
            public void onSuccess(String s) {
                requestEnd = true;
                if (animationEnd)
                    sendMessage();
            }

            @Override
            public void onError(RongIMClient.ErrorCode code) {
                Toast.makeText(SplashActivity.this, "登录失败" + code.getMessage(), Toast.LENGTH_SHORT).show();
                requestEnd = true;
                handler.sendEmptyMessageDelayed(0,500);
            }
        });
    }


    private void requestForToken() {

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher requestToken=retrofit.create(InterfaceTeacher.class);
        final Call<Result> call=requestToken.requestToken(StaticInfo.uid);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().getCode() == 0) {
                    StaticInfo.token=response.body().getToken();
                    connect();
                }
                else{
                    new AlertDialog.Builder(SplashActivity.this).setMessage("获取token失败").show();
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                new AlertDialog.Builder(SplashActivity.this).setMessage("获取token失败:" + t.toString()).show();
            }
        });
    }

    private void startAnimation() {
        showLogo = new AlphaAnimation(0, 1);
        showLogo.setDuration(800);
        showTitleLayout = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showTitleLayout.setDuration(800);
        showTitleView = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showTitleView.setDuration(800);
        showTitleView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationEnd = true;
                if (requestEnd)
                    sendMessage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flContainer.setVisibility(View.VISIBLE);
                flContainer.startAnimation(showTitleLayout);
                tvTitle.startAnimation(showTitleView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.startAnimation(showLogo);
    }

    private void sendMessage() {
        handler.sendEmptyMessageDelayed(needLogin ? 0 : 1, 500);
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
            Log.i("userinfo", "findUserByUid: "+info);
            if (info!=null)
                userInfo = new io.rong.imlib.model.UserInfo(info.getUid(), info.getNickname(), Uri.parse(getResources().getString(R.string.baseURL) + info.getImageURL()));
            return userInfo;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */

}
