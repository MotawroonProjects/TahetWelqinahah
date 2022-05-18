package com.lost_found_it.uis.activity_contact_us;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lost_found_it.R;
import com.lost_found_it.databinding.ActivityContactUsBinding;
import com.lost_found_it.model.ContactUsModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.mvvm.ActivityContactUsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.preferences.Preferences;
import com.lost_found_it.uis.activity_base.BaseActivity;

public class ContactUsActivity extends BaseActivity {
    private ActivityContactUsBinding binding;
    private ContactUsModel contactUsModel;
    private UserModel userModel;
    private Preferences preferences;
    private ActivityContactUsMvvm mvvm;
    private GeneralMvvm generalMvvm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
//        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(ActivityContactUsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        contactUsModel = new ContactUsModel();
        if (userModel != null) {
            contactUsModel.setName(userModel.getData().getUser().getFirst_name()+" "+userModel.getData().getUser().getLast_name());
            if(userModel.getData().getUser().getEmail()!=null) {
                contactUsModel.setEmail(userModel.getData().getUser().getEmail());
            }
            contactUsModel.setPhone(userModel.getData().getUser().getPhone());
        }

        binding.setContactModel(contactUsModel);

        binding.btnSend.setOnClickListener(view -> {
            if (contactUsModel.isDataValid(this)) {
                mvvm.contactUs(this, contactUsModel,getUserSetting().getCountry());
            }
        });
        mvvm.send.observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }


}