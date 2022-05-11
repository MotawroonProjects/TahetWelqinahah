package com.lost_found_it.uis.activity_contact_us;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lost_found_it.R;
import com.lost_found_it.databinding.ActivityContactUsBinding;
import com.lost_found_it.model.ContactUsModel;
import com.lost_found_it.uis.activity_base.BaseActivity;

public class ContactUsActivity extends BaseActivity {
    private ActivityContactUsBinding binding;
    private ContactUsModel contactUsModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black);

        contactUsModel = new ContactUsModel();


        binding.setContactModel(contactUsModel);

        binding.btnSend.setOnClickListener(view -> {

        });

        binding.toolbar.llBack.setOnClickListener(view -> finish());
    }


}