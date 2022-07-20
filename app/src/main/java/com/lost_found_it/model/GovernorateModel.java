package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class GovernorateModel implements Serializable {
    private String id;
    private String name;
    private List<CityModel> cities;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CityModel> getCities() {
        return cities;
    }
}
