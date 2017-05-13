package com.example.mrwen.bean;

/**
 * Created by mrwen on 2017/2/26.
 */

public class TotalCourseLearning {
    int id;
    long duration;
    Course course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
