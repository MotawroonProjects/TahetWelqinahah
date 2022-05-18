package com.lost_found_it.model;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.lost_found_it.BR;
import com.lost_found_it.R;

import java.io.Serializable;


public class ContactUsModel extends BaseObservable implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String text;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_text = new ObservableField<>();

    public boolean isDataValid(Context context) {

        if (!name.isEmpty() &&
                !email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !phone.isEmpty() &&
                !text.isEmpty()

        ) {
            error_name.set(null);
            error_email.set(null);
            error_phone.set(null);
            error_text.set(null);

            return true;

        } else {
            if (name.isEmpty()){
                error_name.set(context.getString(R.string.field_required));
            }else {
                error_name.set(null);

            }


            if (email.isEmpty()){
                error_email.set(context.getString(R.string.field_required));
            }if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                error_email.set(context.getString(R.string.inv_email));
            }else {
                error_email.set(null);

            }

            if (phone.isEmpty()){
                error_phone.set(context.getString(R.string.field_required));
            }else {
                error_phone.set(null);

            }

            if (text.isEmpty()){
                error_text.set(context.getString(R.string.field_required));
            }else {
                error_text.set(null);

            }

            return false;
        }

    }

    public ContactUsModel() {
        name = "";
        email = "";
        phone = "";
        text = "";
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
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);

    }


}
