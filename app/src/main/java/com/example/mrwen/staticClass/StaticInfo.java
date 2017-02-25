package com.example.mrwen.staticClass;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by mrwen on 2017/2/9.
 */

public  class StaticInfo {
    public static int id;

    public static String token;

    public static String identity;
    public static String username;
    public static String password;
    public static String realname;
    public static String gender;
    public static String region;
    public static String rank;
    public static String subject;
    public static String phone;
    public static String email;
    public static String signature;
    public static String imageURL;

    public static String currentCourseId;
    public static String currentChapterId;
    public static String currentLessonId;
    public static String currentVideoURL;

    public static ArrayList<String> groupNames=new ArrayList<>();
    public static String  uid;
    public static Uri getPrivateChatUri(String uid, String title) {
        return Uri.parse("rong://com.example.mrwen.teacher/conversation/private?targetId=" + uid + "&title=" + title);
    }



    public static String getIdentity(String uid){
        String identity = "";

        switch (uid.charAt(0)) {
            case 's':
                identity = "学生";
                break;
            case 't':
                identity = "老师";
                break;
            case 'c':
                identity = "大学生";
                break;
            case 'a':
                identity = "家长";
                break;
            default:
                break;
        }
        return identity;
    }

}
