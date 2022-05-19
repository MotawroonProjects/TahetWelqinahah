package com.lost_found_it.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id;
    private String title;
    private String text;
    private String ad_id;
    private String user_id;
    private String time;
    private String date;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getAd_id() {
        return ad_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
