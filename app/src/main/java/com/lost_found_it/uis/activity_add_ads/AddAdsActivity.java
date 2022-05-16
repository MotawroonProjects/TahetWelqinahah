package com.lost_found_it.uis.activity_add_ads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lost_found_it.R;
import com.lost_found_it.adapter.MyPagerAdapter;
import com.lost_found_it.databinding.ActivityAddAdsBinding;
import com.lost_found_it.model.AddAdModel;
import com.lost_found_it.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AddAdsActivity extends BaseActivity {
    private ActivityAddAdsBinding binding;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ads);
        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolBar, getString(R.string.post_your_ad), R.color.white, R.color.black);
        fragments = new ArrayList<>();
        fragments.add(FragmentAddAdStep1.newInstance());
        fragments.add(FragmentAddAdStep2.newInstance());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);


    }

    public void navigateToStep2(AddAdModel model) {
        binding.pager.setCurrentItem(1);
        FragmentAddAdStep2 fragmentAddAdStep2 = (FragmentAddAdStep2) adapter.getItem(1);
        fragmentAddAdStep2.updateModel(model);
    }

    public void navigateToStep1() {
        binding.pager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        if (binding.pager.getCurrentItem()==0){
            super.onBackPressed();

        }else {
            navigateToStep1();
        }

    }
}