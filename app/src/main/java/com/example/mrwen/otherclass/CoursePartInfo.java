package com.example.mrwen.otherclass;

/**
 * Created by mrwen on 2016/10/30.
 */

public class CoursePartInfo {
    private String courseId;

    private String courseName;
    private String courseSubject;
    private String courseChapter;
    private String courseCover;

    public CoursePartInfo(String courseName, String courseSubject, String courseChapter, String courseCover){
        this.courseName=courseName;
        this.courseSubject=courseSubject;
        this.courseChapter=courseChapter;
        this.courseCover=courseCover;
    }
    public String getCourseId() {
        return courseId;
    }
    public String  getCourseName(){
        return courseName;
    }
    public String getCourseSubject(){
        return courseSubject;
    }
    public String getCourseChapter(){
        return courseChapter;
    }
    public String getCourseCover(){return courseCover;}



    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseSubject(String courseSubject) {
        this.courseSubject = courseSubject;
    }

    public void setCourseChapter(String courseChapter) {
        this.courseChapter = courseChapter;
    }

    public void courseCover(String courseCover) {
        this.courseCover = courseCover;
    }

}
