package com.example.mrwen.otherclass;

/**
 * Created by mrwen on 2016/11/29.
 */

public class CourseInfo {
    private String courseName;
    private String grade;
    private String subject;
    private String textbook;
    private String introduce;

    public CourseInfo(String courseName, String grade, String subject, String textbook, String introduce) {
        this.courseName = courseName;
        this.grade = grade;
        this.subject = subject;
        this.textbook = textbook;
        this.introduce = introduce;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
