package com.example.mrwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.bean.GetVideoResult;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceCourse;
import com.example.mrwen.interfaces.InterfaceLesson;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoCheck extends AppCompatActivity {

    @Bind(R.id.tv_video_name)
    TextView tv_video_name;
    @Bind(R.id.iv_video_image)
    ImageView iv_video_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_check);
        ButterKnife.bind(this);
        retrofitGetVideo();

        iv_video_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWifi(VideoCheck.this)){
                    Intent intentVideo = new Intent(VideoCheck.this, VideoActivity.class);
                    startActivity(intentVideo);
                }
                else {
                    new AlertDialog.Builder(VideoCheck.this).setTitle("提示")
                            .setMessage("当前为非Wifi网络状态，确认要播放么")
                            .setPositiveButton("确认播放", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    retrofitGetVideo();
                                }
                            })
                            .setNegativeButton("取消播放", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });
    }

    //判断是否为wifi状态
    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private void retrofitGetVideo(){
        final MyDialog alertDialog=new MyDialog();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceLesson getLessonVideo=retrofit.create(InterfaceLesson.class);

        final Call<GetVideoResult> call=getLessonVideo.getLessonVideo(Integer.parseInt(StaticInfo.currentLessonId));
        call.enqueue(new Callback<GetVideoResult>() {
            @Override
            public void onResponse(Call<GetVideoResult> call, Response<GetVideoResult> response) {
                if(response.body().getResultCode()==1){
                    Log.i("imageURLLLL",getResources().getString(R.string.baseURL)+response.body().getVideoImageURL());
                    if(response.body().getVideoImageURL()!=null)
                        Glide.with(VideoCheck.this).load(getResources().getString(R.string.baseURL)+response.body().getVideoImageURL())
                                .signature(new StringSignature(UUID.randomUUID().toString())).into(iv_video_image);
                    tv_video_name.setText(response.body().getLessonName());
                    StaticInfo.currentVideoURL=getResources().getString(R.string.baseURL)+response.body().getVideoURL();
                }else {
                    alertDialog.showAlertDialgo(VideoCheck.this,"获取视频失败");
                }
            }
            @Override
            public void onFailure(Call<GetVideoResult> call, Throwable t) {
                alertDialog.showAlertDialgo(VideoCheck.this,t.toString());
            }
        });
    }

}
