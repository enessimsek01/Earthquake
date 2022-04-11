package com.enessimsek.earthquakeproject.entity;

import java.util.Date;

public class Earthquake {
    private float magnitude;

    private  String location;

    private long timeInMilliseconds;

    private String Url;

    private Date date;

    public Earthquake(float magnitude, String location, long timeInMilliseconds, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.timeInMilliseconds = timeInMilliseconds;
        Url = url;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Date getDate() {
        if(timeInMilliseconds>0){
            return new Date(timeInMilliseconds);
        }
        return null;
    }
}
