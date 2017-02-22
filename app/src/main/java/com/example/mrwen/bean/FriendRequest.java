package com.example.mrwen.bean;

/**
 * Created by fate on 2017/2/14.
 */

public class FriendRequest {
    private int id;
    private String uidFrom;
    private String nickname;
    private String imageURL;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUidFrom() {
        return uidFrom;
    }

    public void setUidFrom(String uidFrom) {
        this.uidFrom = uidFrom;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
