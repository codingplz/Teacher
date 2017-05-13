package com.example.mrwen.bean;

import java.io.Serializable;

/**
 * Created by mrwen on 2017/2/23.
 */

public class AdminClass implements Serializable {
    int id;
    int sNumber;
    String region;
    String school;
    String grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
