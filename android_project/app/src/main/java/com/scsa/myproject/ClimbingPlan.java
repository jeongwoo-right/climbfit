package com.scsa.myproject;

import java.io.Serializable;


public class ClimbingPlan implements Serializable {
    private String place;
    private String date;
    private String partner;
    private String goal;
    private boolean isCompleted;

    public ClimbingPlan(String place, String date, String partner, String goal) {
        this.place = place;
        this.date = date;
        this.partner = partner;
        this.goal = goal;
        this.isCompleted = false;
    }

    public String getPlace() { return place; }
    public String getDate() { return date; }
    public String getPartner() { return partner; }
    public String getGoal() { return goal; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}