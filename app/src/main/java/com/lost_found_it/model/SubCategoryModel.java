package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class SubCategoryModel implements Serializable {
    private String id;
    private String title;
    private boolean isSelected;


    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
