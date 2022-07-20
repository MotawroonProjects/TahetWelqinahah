package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class GovernorateDataModel extends StatusResponse implements Serializable {
    private List<GovernorateModel> data;

    public List<GovernorateModel> getData() {
        return data;
    }
}
