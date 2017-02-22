package com.example.mrwen.bean;

/**
 * Created by mrwen on 2017/2/13.
 */

public class Lesson {
    int id;
    String chapterId;
    String lessonName;
    String knowledgePoint;
    String description;
    String videoURL;
    String videoImageURL;
    int isUpload;

    public String getVideoImageURL() {
        return videoImageURL;
    }

    public void setVideoImageURL(String videoImageURL) {
        this.videoImageURL = videoImageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }
}
