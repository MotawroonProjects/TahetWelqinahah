package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class AdsDataModel extends StatusResponse implements Serializable {
    private List<AdModel> data;

    public List<AdModel> getData() {
        return data;
    }
}
