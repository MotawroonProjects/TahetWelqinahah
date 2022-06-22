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
    private String city_id;
    private String is_followed;
    private String is_loved;
    private String is_bad;
    private String views;
    private UserModel.User user;
    private List<SliderImages> images;
    private SliderImages first_image;
    private CategoryModel category;
    private SubCategoryModel subCategory;
    private String created_at;
    private String distance;


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

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    public void setIs_loved(String is_loved) {
        this.is_loved = is_loved;
    }

    public void setIs_bad(String is_bad) {
        this.is_bad = is_bad;
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

    public String getDistance() {
        return distance;
    }

    public String getIs_loved() {
        return is_loved;
    }

    public String getIs_bad() {
        return is_bad;
    }

    public String getCity_id() {
        return city_id;
    }
}
