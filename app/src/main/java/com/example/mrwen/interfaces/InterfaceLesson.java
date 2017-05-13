package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Chapter;
import com.example.mrwen.bean.GetVideoResult;
import com.example.mrwen.bean.HasIdResult;
import com.example.mrwen.bean.Lesson;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.otherclass.LessonPartInfo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/13.
 */

public interface InterfaceLesson {

    //获得该课程全部课时信息
    @GET("servlet/GetAllLessonInfo")
    Call<List<List<LessonPartInfo>>> getAllLessonInfo(@Query("courseId") int id);

    //获得全部课时信息
    @GET("servlet/GetLessonInfo")
    Call<List<Lesson>> getLessonInfo(@Query("id") int id);

    //获得单个课时信息
    @GET("servlet/GetSingleLessonInfo")
    Call<Lesson> getSingleLessonInfo(@Query("id") int id);

    //修改课时信息
    @FormUrlEncoded
    @POST("servlet/LessonInfoRevise")
    Call<UniversalResult> lessonInfoRevise(@FieldMap Map<String,String> map);

    //上传视频
    @Multipart
    @POST("servlet/LessonUpload")
    Call<HasIdResult> lessonUpload(
            @Part("lessonInfo") String lessonInfo,
            @Part MultipartBody.Part file);

    //获得课时视频
    @GET("servlet/GetLessonVideo")
    Call<GetVideoResult> getLessonVideo(@Query("lessonId") int id);

    //课时封面
    @Multipart
    @POST("servlet/VideoImageUpload")
    Call<UniversalResult> videoImagvUpload(@Part("fileName") String id, @Part("file\";filename=\"*.jpg") RequestBody image);

    //删除课时
    @GET("servlet/DeleteLesson")
    Call<UniversalResult> deleteLesson(@Query("id") int id);

}
