package com.example.mrwen.bean;

/**
 * Created by mrwen on 2017/2/15.
 */

public class GetVideoResult {
    int resultCode;
    String videoURL;
    String videoImageURL;
    String lessonName;

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getVideoURL() {
        return videoURL;
    }
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoImageURL() {
        return videoImageURL;
    }

    public void setVideoImageURL(String videoImageURL) {
        this.videoImageURL = videoImageURL;
    }
}
