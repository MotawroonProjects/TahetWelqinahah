package com.lost_found_it.model;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.lost_found_it.BR;

import java.io.Serializable;


public class ContactUsModel extends BaseObservable implements Serializable {
    private String name;
    private String email;
    private String subject;
    private String message;
    private boolean valid;

    public void isDataValid() {

        if (!name.isEmpty() &&
                !email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !subject.isEmpty() &&
                !message.isEmpty()

        ) {


            setValid(true);

        } else {

            setValid(false);

        }

    }

    public ContactUsModel() {
        name = "";
        email = "";
        subject = "";
        message = "";
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        notifyPropertyChanged(BR.subject);

    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);

    }

    @Bindable
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }
}
