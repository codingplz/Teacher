package com.example.mrwen.bean;

import java.util.Date;

/**
 * Created by mrwen on 2017/2/26.
 */

public class CourseLearning {
    int id;
    long duration;
    Date time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
