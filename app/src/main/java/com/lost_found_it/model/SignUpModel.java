package com.lost_found_it.model;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lost_found_it.BR;

public class SignUpModel extends BaseObservable {
    private String image_uri;
    private String image_path;
    private String phone_code;
    private String phone;
    private String first_name;
    private String last_name;
    private String email;
    private boolean acceptTerms;
    private boolean valid;

    public void isDataValid() {
        if (!first_name.trim().isEmpty() &&
                !last_name.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()&&
                acceptTerms

        ) {
            setValid(true);
        } else {
            setValid(false);


        }
    }

    public SignUpModel() {
        image_uri = "";
        image_path = "";
        phone_code = "";
        phone = "";
        first_name = "";
        last_name = "";
        email = "";
        acceptTerms = false;


    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);
        isDataValid();
    }

    @Bindable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        notifyPropertyChanged(BR.last_name);
        isDataValid();
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
        isDataValid();
    }



    @Bindable
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public boolean isAcceptTerms() {

        return acceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
        isDataValid();
    }
}