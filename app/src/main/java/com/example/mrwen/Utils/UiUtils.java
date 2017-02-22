package com.example.mrwen.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.example.mrwen.activity.App;


/**
 * Created by fate on 2016/10/20.
 */

public class UiUtils {
    public static Context getContext(){
        return App.getContext();
    }
    public static Handler getHandler(){
        return  App.getHandler();
    }
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }
    public static boolean isRunOnUiThread(){
        return Process.myTid() == App.getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable){
        if (isRunOnUiThread()) {
            runnable.run();
        }else {
            getHandler().post(runnable);
        }
    }
}
