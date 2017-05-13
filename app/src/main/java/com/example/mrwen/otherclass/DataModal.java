package com.example.mrwen.otherclass;

import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.CourseLearning;
import com.example.mrwen.bean.Issue;
import com.example.mrwen.bean.MessageRecord;

/**
 * Created by ishratkhan on 24/02/16.
 */
public class DataModal {
    int level;
    int type;
    String name;
    CourseLearning courseLearning;
    Issue issue;
    Answer answer;
    MessageRecord messageRecord;

    public DataModal(int level,int type,String name) {
        this.level = level;
        this.name = name;
        this.type=type;
    }

    public DataModal(int level,String name) {
        this.level = level;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseLearning getCourseLearning() {
        return courseLearning;
    }

    public void setCourseLearning(CourseLearning courseLearning) {
        this.courseLearning = courseLearning;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public MessageRecord getMessageRecord() {
        return messageRecord;
    }

    public void setMessageRecord(MessageRecord messageRecord) {
        this.messageRecord = messageRecord;
    }
}
