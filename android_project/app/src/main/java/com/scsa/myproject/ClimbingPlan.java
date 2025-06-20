package com.scsa.myproject;

import java.io.Serializable;


public class ClimbingPlan implements Serializable {
    private String place;
    private String date;
    private String partner;
    private String goal;
    private boolean isCompleted;
    private String record;

    public ClimbingPlan(String place, String date, String partner, String goal) {
        this.place = place;
        this.date = date;
        this.partner = partner;
        this.goal = goal;
        this.isCompleted = false;
        this.record = "";  // 초기 빈 문자열
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPlace() { return place; }
    public String getDate() { return date; }
    public String getPartner() { return partner; }
    public String getGoal() { return goal; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getRecord() { return record; }
    public void setRecord(String record) { this.record = record; }
}