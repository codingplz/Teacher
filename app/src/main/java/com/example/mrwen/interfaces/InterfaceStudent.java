package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.CourseLearning;
import com.example.mrwen.bean.DayStudyInfo;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.MessageRecord;
import com.example.mrwen.bean.Student;
import com.example.mrwen.bean.TotalCourseLearning;
import com.example.mrwen.bean.TotalMessageRecord;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/26.
 */

public interface InterfaceStudent {
    //获得课程学习情况
    @GET("servlet/GetCourseLearning")
    Call<ArrayList<CourseLearning>> getCourseLearning(@Query("id") int id);

    //获得课程学习总情况
    @GET("servlet/GetTotalCourseLearning")
    Call<ArrayList<TotalCourseLearning>> getTotalCourseLearning(@Query("id") int id);

    //获得消息发送情况
    @GET("servlet/GetMessageRecord")
    Call<ArrayList<MessageRecord>> getMessageRecord(@Query("uid") String uid);

    //获得消息发送总情况
    @GET("servlet/GetTotalMessageRecord")
    Call<ArrayList<TotalMessageRecord>> getTotalMessageRecord(@Query("uid") String uid);

    //获得学习总情况
    @GET("servlet/GetStudyInfo")
    Call<ArrayList<DayStudyInfo>> getStudyInfo(@Query("id") int id);

    //获得单个问题
    @GET("servlet/GetSingleIssue")
    Call<Issue> getSingleIssue(@Query("issueId") int id);

    //获得单个回答
    @GET("servlet/GetSingleAnswer")
    Call<Answer> getSingleAnswer(@Query("answerId") int id);
}
