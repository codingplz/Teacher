package com.example.mrwen.bean;

import java.util.Date;

/**
 * Created by fate on 2016/12/2.
 */

public class Rank {
    private String content;
    private float rank;
    private User ranker;
    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Rank(String content, float rank, User ranker) {
        this.content = content;
        this.rank = rank;
        this.ranker = ranker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public User getRanker() {
        return ranker;
    }

    public void setRanker(User ranker) {
        this.ranker = ranker;
    }
}
