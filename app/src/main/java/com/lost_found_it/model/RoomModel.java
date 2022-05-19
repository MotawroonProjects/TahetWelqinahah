package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class RoomModel implements Serializable {
    private String id;
    private String to_user_id;
    private String from_user_id;
    private String ad_id;
    private AdModel ad;
    private UserModel.User to_user;
    private UserModel.User from_user;
    private UserModel.User user;
    private MessageModel latest_message;

    private List<MessageModel> messages;

    public String getId() {
        return id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public AdModel getAd() {
        return ad;
    }

    public UserModel.User getTo_user() {
        return to_user;
    }

    public UserModel.User getFrom_user() {
        return from_user;
    }

    public UserModel.User getUser() {
        return user;
    }

    public MessageModel getLatest_message() {
        return latest_message;
    }

    public List<MessageModel> getMessages() {
        return messages;
    }
}
