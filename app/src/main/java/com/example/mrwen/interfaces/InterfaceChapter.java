package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Chapter;
import com.example.mrwen.bean.Lesson;
import com.example.mrwen.bean.UniversalResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/13.
 */

public interface InterfaceChapter {
    //上传章节
    @FormUrlEncoded
    @POST("servlet/ChapterUpload")
    Call<UniversalResult> chapterUpload(@FieldMap Map<String,String> map);

    //获得全部章节信息
    @GET("servlet/GetChapterInfo")
    Call<List<Chapter>> getChapterInfo(@Query("id") int id);

    //获得单个章节信息
    @GET("servlet/GetSingleChapterInfo")
    Call<Chapter> getSingleChapterInfo(@Query("id") int id);

    //修改章节信息
    @FormUrlEncoded
    @POST("servlet/ChapterInfoRevise")
    Call<UniversalResult> chapterInfoRevise(@FieldMap Map<String,String> map);


    //已上传课时数加一
    @FormUrlEncoded
    @POST("servlet/LessonNumberPlus")
    Call<UniversalResult> LessonNumberPlus(@Field("chapterId") int id);

    //删除单元
    @GET("servlet/DeleteChapter")
    Call<UniversalResult> deleteChapter(@Query("id") int id);

}
