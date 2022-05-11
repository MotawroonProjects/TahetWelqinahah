package com.lost_found_it.model;

import java.io.Serializable;

public class BrandModel implements Serializable {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
