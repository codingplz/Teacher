package com.example.mrwen.activity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import io.rong.imkit.RongIM;

/**
 * Created by mrwen on 2017/2/15.
 */

public class App extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        context=getApplicationContext();
        handler = new Handler(getMainLooper());
        mainThreadId = Process.myTid();
        Log.i("package", "onCreate: "+context.getPackageName());
    }
    public static Context getContext(){
        return context;
    }
    public static Handler getHandler(){
        return handler;
    }
    public static int getMainThreadId(){
        return mainThreadId;
    }
}