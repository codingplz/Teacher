package com.example.mrwen.otherclass;

/**
 * Created by mrwen on 2016/10/30.
 */

public class Question {
    private String courseName;
    private String chapterName;
    private String question;
    private String followStudent;
    private String isTag;
    private int imageId;

    public Question(String courseName,String chapterName,String question,String followStudent,String isTag,int imageId){
        this.courseName=courseName;
        this.chapterName=chapterName;
        this.question=question;
        this.followStudent=followStudent;
        this.isTag=isTag;
        this.imageId=imageId;
    }
    public String getChapterName() {
        return chapterName;
    }

    public String getQuestion() {
        return question;
    }

    public String getFollowStudent() {
        return followStudent;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getIsTag() {
        return isTag;
    }

    public int getImageId() {
        return imageId;
    }
}
