package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class AdModel implements Serializable {
    private String id;
    private String title;
    private String description;
    private String type;
    private String place_type;
    private String address;
    private String latitude;
    private String longitude;
    private String phone_code;
    private String phone;
    private String whatsapp;
    private String is_followed;
    private String views;
    private UserModel.User user;
    private List<SliderImages> images;
    private SliderImages first_image;
    private CategoryModel category;
    private SubCategoryModel subCategory;
    private String created_at;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getPlace_type() {
        return place_type;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public String getViews() {
        return views;
    }

    public UserModel.User getUser() {
        return user;
    }

    public List<SliderImages> getImages() {
        return images;
    }

    public SliderImages getFirst_image() {
        return first_image;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public SubCategoryModel getSubCategory() {
        return subCategory;
    }

    public String getCreated_at() {
        return created_at;
    }
}
