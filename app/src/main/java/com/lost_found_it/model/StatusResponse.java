package com.lost_found_it.model;

import java.io.Serializable;

public class StatusResponse implements Serializable {
    protected int code;
    protected Object message;

    public int getCode() {
        return code;
    }

    public Object getMessage() {
        return message;
    }
}
