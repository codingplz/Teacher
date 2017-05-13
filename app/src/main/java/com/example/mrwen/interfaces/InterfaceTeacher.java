package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.ChatGroup;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.bean.ExerciseCatagory;
import com.example.mrwen.bean.FriendRequest;
import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.InfoDetail;
import com.example.mrwen.bean.LoginInResult;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.RegisterResult;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.RosterGroup;
import com.example.mrwen.bean.UniversalResult;

import java.util.ArrayList;
import java.util.Map;

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
 * Created by mrwen on 2017/2/14.
 */

public interface InterfaceTeacher {
    @FormUrlEncoded
    @POST("servlet/Register")
    Call<RegisterResult> register(@FieldMap Map<String,String> map);

    //接受好友请求
    @POST("servlet/IsUsernameUnique")
    @FormUrlEncoded
    Call<UniversalResult> isUsernameUnique(@Field("username") String username);

    @FormUrlEncoded
    @POST("servlet/LoginIn")
    Call<LoginInResult> loginIn(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("servlet/TeacherInfoRevise")
    Call<UniversalResult> teacherInfoRevise(@FieldMap Map<String,String> map);

    @Multipart
    @POST("servlet/ImageUpload")
    Call<UniversalResult> imageUpload(@Part("fileName") String id, @Part("file\";filename=\"*.jpg") RequestBody image);

    //接受好友请求
    @POST("AddFriendServlet")
    @FormUrlEncoded
    Call<UniversalResult> addFriend(@Field("rid")int rid , @Field("group")String group, @Field("remark")String remark);

    //发送好友请求
    @POST("RequestFriendServlet")
    @FormUrlEncoded
    Call<UniversalResult> requestFriend(@Field("quid")String quid ,@Field("muid")String muid ,@Field("group")String group,@Field("remark")String remark,@Field("message")String message);

    //获取好友请求
    @GET("GetFriendRequestServlet")
    Call<ArrayList<FriendRequest>> getFriendRequest(@Query("uid") String uid);

    //好友请求数目
    @GET("GetRequestCountServlet")
    Call<Result> getRequestCount(@Query("uid") String uid);

    //查找好友
    @GET("QueryContactsServlet")
    Call<ArrayList<QueryItem>> queryContacts(@Query("uid")String uid, @Query("query") String query);

    //获取自己的分组
    @GET("GetGroupNamesServlet")
    Call<ArrayList<String>> getGroupNames(@Query("uid") String uid);

    //获取好友来显示
    @GET("GetContactsServlet")
    Call<ArrayList<RosterGroup>> getContacts(@Query("uid") String uid);

    //获取详细信息
    @GET("GetDetailedInfoServlet")
    Call<InfoDetail> getDetailedInfo(@Query("uid") String uid);

    //获得联系人信息
    @GET("GetUserInfoServlet")
    Call<Info> getUserInfo(@Query("quid")String queryUid, @Query("muid")String myUid);

    //获得token
    @GET("RequestTokenServlet")
    Call<Result> requestToken(@Query("uid") String uid);

    //获取我的回答
    @GET("LoadMyAnswersServlet")
    Call<ArrayList<Answer>> loadMyAnswer(@Query("uid")String uid);

    //删除回答
    @GET("DeleteAnswerServlet")
    Call<Result> deleteAnswer(@Query("aid")int aid);

    //修改密码
    @FormUrlEncoded
    @POST("servlet/PasswordRevise")
    Call<UniversalResult> passwordRevise(@Field("uid")String uid ,@Field("password")String password );

    //发送好友请求
    @POST("servlet/CreateClass")
    @FormUrlEncoded
    Call<UniversalResult> createClass(@FieldMap Map<String,String> map);

    //获取群组
    @GET("GetGroupsServlet")
    Call<ArrayList<ChatGroup>> getGroups(@Query("uid") String uid);

    //获取题目
    @GET("servlet/GetTest")
    Call<ArrayList<Exercise>> getTest(@Query("tid") String tid);

    //获取单个题目
    @GET("servlet/GetSingleExercise")
    Call<Exercise> getSingleExercise(@Query("exerciseId") String id);

    //上传题目
    @FormUrlEncoded
    @POST("servlet/UploadTest")
    Call<UniversalResult> uploadTest(@FieldMap Map<String,String> map);

    //获取题集
    @GET("servlet/GetExerciseCatagory")
    Call<ExerciseCatagory> getExerciseCatagory(@Query("id") String id);

    //获取题集内题目
    @GET("servlet/GetExercise")
    Call<ArrayList<Exercise>> getExercise(@Query("id") String id);


}
