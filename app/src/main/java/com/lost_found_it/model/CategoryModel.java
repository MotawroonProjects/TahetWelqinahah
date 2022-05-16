package com.lost_found_it.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel implements Serializable {
    private String id;
    private String title;
    private String is_special;
    private String image;
    private List<SubCategoryModel> sub_categories = new ArrayList<>();
    private String created_at;
    private String updated_at;
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

    public String getIs_special() {
        return is_special;
    }

    public String getImage() {
        return image;
    }

    public List<SubCategoryModel> getSub_categories() {
        return sub_categories;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setSub_categories(List<SubCategoryModel> sub_categories) {
        this.sub_categories = sub_categories;
    }
}
