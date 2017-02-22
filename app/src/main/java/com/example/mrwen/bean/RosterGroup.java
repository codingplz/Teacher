package com.example.mrwen.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fate on 2017/2/10.
 */

public class RosterGroup {
    private int id;
    private String name;
    private String uno;
    private ArrayList<Roster> rosters;

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

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public ArrayList<Roster> getRosters() {
        return rosters;
    }

    public void setRosters(ArrayList<Roster> rosters) {
        this.rosters = rosters;
    }
}
