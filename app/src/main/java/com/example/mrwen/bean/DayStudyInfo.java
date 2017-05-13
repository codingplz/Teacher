package com.example.mrwen.bean;

import com.example.mrwen.otherclass.Question;

import java.util.Date;
import java.util.List;

/**
 * Created by mrwen on 2017/2/26.
 */

public class DayStudyInfo {
    Date time;
    List<CourseLearning> learnings;
    List<Issue> issues;
    List<Answer> answers;
    List<MessageRecord> messages;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<CourseLearning> getLearnings() {
        return learnings;
    }

    public void setLearnings(List<CourseLearning> learnings) {
        this.learnings = learnings;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<MessageRecord> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageRecord> messages) {
        this.messages = messages;
    }
}
