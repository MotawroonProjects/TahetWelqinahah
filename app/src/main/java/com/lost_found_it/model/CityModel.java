package com.lost_found_it.model;

import java.io.Serializable;

public class CityModel implements Serializable {
    private String id;
    private String name;
    private String governorate;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGovernorate() {
        return governorate;
    }
}
