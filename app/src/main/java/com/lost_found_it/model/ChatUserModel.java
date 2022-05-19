package com.lost_found_it.model;

import java.io.Serializable;

public class ChatUserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_phone;
    private String user_image;
    private String ad_id;
    private String room_id;
    private AdModel adModel;

    public ChatUserModel(String user_id, String user_name, String user_phone, String user_image, String ad_id, String room_id, AdModel adModel) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_image = user_image;
        this.ad_id = ad_id;
        this.room_id = room_id;
        this.adModel = adModel;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getAd_id() {
        return ad_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public AdModel getAdModel() {
        return adModel;
    }
}
