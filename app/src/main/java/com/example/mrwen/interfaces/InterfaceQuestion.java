package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/17.
 */

public interface InterfaceQuestion {
    @GET("AgreeAnswerServlet")
    Call<Result> agreeAnswer(@Query("aid") int aid);

    @POST("WriteAnswerServlet")
    @FormUrlEncoded
    Call<Result> submitAnswer(@Field("content") String content, @Field("anonymous") boolean anonymous, @Field("iid") int iid, @Field("uid")String uid);

    @GET("LoadAnswersServlet")
    Call<ArrayList<Answer>> loadAnswers(@Query("iid") int iid);

    @GET("LoadDiscoverServlet")
    Call<ArrayList<Issue>> loadDiscover(@Query("start") int start);
}
