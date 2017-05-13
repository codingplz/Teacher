package com.example.mrwen.interfaces;

import com.example.mrwen.bean.AdminClass;
import com.example.mrwen.bean.Course;
import com.example.mrwen.bean.Student;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/23.
 */

public interface InterfaceClass {

    //获得班级列表
    @GET("servlet/GetClass")
    Call<ArrayList<AdminClass>> getClass(@Query("courseId") int id);

    //获得班级信息
    @GET("servlet/GetClassInfo")
    Call<AdminClass> getClassInfo(@Query("classId") int id);

    //获得学生列表
    @GET("servlet/GetStudentList")
    Call<ArrayList<Student>> getStudentList(@Query("classId") int id);
}
