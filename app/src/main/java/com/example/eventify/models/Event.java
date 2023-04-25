package com.example.eventify.models;

import java.util.Date;

public class Event {
    private long date;
    private String title;
    private String description;
    private Date startDate;

    public Event(long date, String title, String description, Date startDate) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
