package com.example.mrwen.bean;

/**
 * Created by mrwen on 2017/2/10.
 */

public class Course {
    private int id;

    private String name;
    private String focusNumber;
    private String chapterNumber;
    private String coverURL;
    private String description;
    private String grade;
    private String subject;
    private String unitNumber;
    private String loadedNumber;
    private String semester;
    private int teacherID;
    private String major;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLoadedNumber() {
        return loadedNumber;
    }

    public void setLoadedNumber(String loadedNumber) {
        this.loadedNumber = loadedNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFocusNumber() {
        return focusNumber;
    }
    public void setFocusNumber(String focusNumber) {
        this.focusNumber = focusNumber;
    }
    public String getChapterNumber() {
        return chapterNumber;
    }
    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
    public String getCoverURL() {
        return coverURL;
    }
    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

}
