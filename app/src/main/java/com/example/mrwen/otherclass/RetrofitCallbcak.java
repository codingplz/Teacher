package com.example.mrwen.otherclass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 60440 on 2017/2/14.
 */

public abstract class RetrofitCallbcak<T> implements Callback<T>{
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onSuccess(call,response);
    }
    public abstract void onSuccess(Call<T> call, Response<T> response);

    public void onLoading(long total,long process){

    }
}
