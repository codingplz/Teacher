package com.example.mrwen.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fate on 2016/12/9.
 */

public class Answer {

    private int id;
    private String content;
    private Info answerer;
    private boolean anonymous;
    private Date time;
    private int agree;
    private Issue issue;

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Info getAnswerer() {
        return answerer;
    }

    public void setAnswerer(Info answerer) {
        this.answerer = answerer;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }


}
