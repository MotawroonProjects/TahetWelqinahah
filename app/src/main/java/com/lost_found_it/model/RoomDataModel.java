package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class RoomDataModel extends StatusResponse implements Serializable {
    private List<RoomModel> data;

    public List<RoomModel> getData() {
        return data;
    }
}
