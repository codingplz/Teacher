package com.example.mrwen.otherclass;

/**
 * Created by mrwen on 2017/2/12.
 */

public class ChapterPartInfo {
    int id;
    String index;
    String name;
    String lessonNumber;
    int isSigned;

    public String getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(int isSigned) {
        this.isSigned = isSigned;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }
}
