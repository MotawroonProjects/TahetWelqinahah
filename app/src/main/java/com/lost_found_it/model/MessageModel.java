package com.lost_found_it.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String id;
    private String user_id;
    private String type;
    private String message;
    private String file;
    private String time;
    private String date;
    private UserModel.User user;

    public MessageModel() {
    }

    public MessageModel(String id, String user_id, String type, String message, String file, String time, String date, UserModel.User user) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.message = message;
        this.file = file;
        this.time = time;
        this.date = date;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getFile() {
        return file;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public UserModel.User getUser() {
        return user;
    }
}
