package com.lost_found_it.model;

import java.io.Serializable;

public class AddChatMessageModel implements Serializable {

    private String type;
    private String text = "";
    private String image;
    private String ad_id;
    private String room_id;
    private String user_id;

    public AddChatMessageModel(String type, String text, String image, String ad_id, String room_id, String user_id) {
        this.type = type;
        this.text = text;
        this.image = image;
        this.ad_id = ad_id;
        this.room_id = room_id;
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getAd_id() {
        return ad_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
