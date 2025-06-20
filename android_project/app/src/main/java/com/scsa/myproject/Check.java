package com.scsa.myproject;

import java.util.Date;

public class Check {
    public long id;
    public String title;
    public String link;
    public String description;
    public Date pubDate;
    public String subject;
    public String category;

    @Override
    public String toString() {
        return "HaniItem{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                ", subject=" + subject +
                ", category=" + category +
                '}';
    }
}
