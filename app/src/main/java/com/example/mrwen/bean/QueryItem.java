package com.example.mrwen.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fate on 2017/2/12.
 */

public class QueryItem
{
    private int type;
    private String title;
    private ArrayList<Info> infos;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfos(ArrayList<Info> infos) {
        this.infos = infos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }



    public ArrayList<Info> getInfos() {
        return infos;
    }


}
