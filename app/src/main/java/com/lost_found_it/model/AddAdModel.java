package com.lost_found_it.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lost_found_it.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddAdModel extends BaseObservable implements Serializable {
    private String ad_id;
    private String ad_type;
    private String country;
    private String place_type; // mecca -- tower
    private String title;
    private String description;
    private String category_id;
    private String sub_category_id;
    private String address;
    private double lat;
    private double lng;
    private List<String> images;
    private String phone_code;
    private String phone;
    private String whatsapp;
    private String city_id;
    private boolean agree_terms;
    private boolean validStep1;
    private boolean validStep2;
    private boolean hasSubCategory;
    private String action;
    private List<String> onlineImages;

    public AddAdModel() {
        city_id = "";
        action = "add";
        ad_id = "";
        phone_code = "+966";
        ad_type = "";
        country = "";
        place_type = "main";
        title = "";
        description = "";
        category_id = "";
        sub_category_id = "";
        hasSubCategory = false;
        address = "";
        lat = 0.0;
        lng = 0.0;
        phone = "";
        whatsapp = "";
        agree_terms = false;
        validStep1 = false;
        validStep2 = false;
        images = new ArrayList<>();
        onlineImages = new ArrayList<>();
    }

    public void isStep1Valid() {
        if (!ad_type.isEmpty() &&
                !country.isEmpty() &&
                !title.isEmpty() &&
                !description.isEmpty()
        ) {
            if (action.equals("add")) {
                if (images.size() > 0) {
                    setValidStep1(true);

                } else {
                    setValidStep1(false);

                }
            } else {
                setValidStep1(true);

            }
        } else {
            setValidStep1(false);
        }
    }

    public void isStep2Valid() {
        if (!category_id.isEmpty() &&
                !address.isEmpty() &&
                !phone.isEmpty() &&
                !whatsapp.isEmpty() &&
                !city_id.isEmpty()&&
                agree_terms
        ) {
            if (hasSubCategory) {
                if (!sub_category_id.isEmpty()) {
                    setValidStep2(true);

                } else {
                    setValidStep2(false);

                }

            } else {
                setValidStep2(true);

            }
        } else {
            setValidStep2(false);
        }
    }

    @Bindable
    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
        notifyPropertyChanged(BR.ad_type);
        isStep1Valid();

    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
        isStep1Valid();

    }

    @Bindable
    public String getPlace_type() {
        return place_type;
    }

    public void setPlace_type(String place_type) {
        this.place_type = place_type;
        notifyPropertyChanged(BR.place_type);
        isStep1Valid();

    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        isStep1Valid();

    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
        isStep1Valid();

    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
        isStep2Valid();
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
        isStep2Valid();

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
        isStep2Valid();


    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
        isStep2Valid();


    }

    @Bindable
    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
        notifyPropertyChanged(BR.whatsapp);
        isStep2Valid();

    }

    public boolean isAgree_terms() {
        return agree_terms;
    }


    @Bindable
    public void setAgree_terms(boolean agree_terms) {
        this.agree_terms = agree_terms;
        notifyPropertyChanged(BR.agree_terms);
        isStep2Valid();


    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Bindable
    public boolean isValidStep1() {
        return validStep1;
    }

    public void setValidStep1(boolean validStep1) {
        this.validStep1 = validStep1;
        notifyPropertyChanged(BR.validStep1);

    }

    @Bindable
    public boolean isValidStep2() {
        return validStep2;
    }

    public void setValidStep2(boolean validStep2) {
        this.validStep2 = validStep2;
        notifyPropertyChanged(BR.validStep2);
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public boolean isHasSubCategory() {
        return hasSubCategory;
    }

    public void setHasSubCategory(boolean hasSubCategory) {
        this.hasSubCategory = hasSubCategory;
        isStep2Valid();
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getOnlineImages() {
        return onlineImages;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setOnlineImages(List<String> onlineImages) {
        this.onlineImages = onlineImages;
    }
}
