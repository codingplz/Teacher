package com.example.mrwen.otherclass;

/**
 * Created by mrwen on 2016/11/16.
 */

public class Message{
    private String name;
    private String time;
    private String content;
    private int imageId;

    public Message(String name,String time,String content,int imageId){
        this.name=name;
        this.time=time;
        this.content=content;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public String getTime(){
        return time;
    }
    public String getContent(){
        return content;
    }
    public int getImageId(){return imageId;}


}
