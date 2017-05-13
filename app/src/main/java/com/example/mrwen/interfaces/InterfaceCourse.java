package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.CourseResult;
import com.example.mrwen.bean.Notice;
import com.example.mrwen.bean.Rank;
import com.example.mrwen.bean.RegisterResult;
import com.example.mrwen.bean.UniversalResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/10.
 */

public interface InterfaceCourse {

    //上传课程
    @FormUrlEncoded
    @POST("servlet/CourseUpload")
    Call<CourseResult> courseUpload(@FieldMap Map<String,String> map);

    //获得课程信息显示
    @GET("servlet/GetCourseInfo")
    Call<List<Course>> getCourseInfo(@Query("id") int id);

    //获得单个课程信息
    @GET("servlet/GetSingleCourseInfo")
    Call<Course> getSingleCourseInfo(@Query("id") int id);

    //修改课程信息
    @FormUrlEncoded
    @POST("servlet/CourseInfoRevise")
    Call<UniversalResult> courseInfoRevise(@FieldMap Map<String,String> map);

    //课程封面
    @Multipart
    @POST("servlet/CoverUpload")
    Call<UniversalResult> coverUpload(@Part("fileName") String id, @Part("file\";filename=\"*.jpg") RequestBody image);

    @GET("LoadRanksServlet")
    Call<ArrayList<Rank>> loadRanks(@Query("cid")int cid);

    //已上传单元数加一
    @FormUrlEncoded
    @POST("servlet/ChapterNumberPlus")
    Call<UniversalResult> chapterNumberPlus(@Field("courseId") int id);

    //删除课程
    @GET("servlet/DeleteCourse")
    Call<UniversalResult> deleteCourse(@Query("id") int id);

    //上传课程公告
    @FormUrlEncoded
    @POST("servlet/NoticeUpload")
    Call<UniversalResult> noticeUpload(@FieldMap Map<String,String> map);

    //获取课程公告
    @GET("servlet/GetNotice")
    Call<ArrayList<Notice>> getNotice(@Query("id") String id);

    //删除课程公告
    @GET("servlet/DeleteNotice")
    Call<UniversalResult> deleteNotice(@Query("id") int id);

}
