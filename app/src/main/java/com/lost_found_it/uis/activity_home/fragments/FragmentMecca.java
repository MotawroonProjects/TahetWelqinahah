package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.databinding.FragmentMeccaBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentMeccaTowerMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentMecca extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentMeccaBinding binding;
    private HomeActivity activity;
    private AdAdapter adAdapter;
    private FragmentMeccaTowerMvvm mvvm;
    private String type="";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentMecca newInstance() {
        return new FragmentMecca();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mecca, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentMeccaTowerMvvm.class);
        binding.setLang(getLang());


        binding.toolbarMecca.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });
        generalMvvm.getOnNewAdAdded().observe(activity, adModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                if (adModel.getPlace_type().equals("special")) {
                    mvvm.getOnDataSuccess().getValue().add(0, adModel);
                    if (adAdapter != null) {
                        adAdapter.notifyItemInserted(0);
                    }
                }

            }
        });

        generalMvvm.getOnAdUpdated().observe(activity, mBoolean -> {
            mvvm.getData(getUserSetting().getCountry(), type);
        });
        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity, list -> {

            binding.recViewLayout.tvNoData.setText(R.string.no_item);
            if (list.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            }
            adAdapter.updateList(list);
        });
        generalMvvm.getOnMeccaFoundLost().observe(activity, type -> {
            this.type = type;
            String title = "";
            if (type.equals("found")) {
                title = getString(R.string.found_things_in_mecca);
            } else {
                title = getString(R.string.missing_things_in_mecca);

            }
            setUpToolbar(binding.toolbarMecca, title, R.color.white, R.color.black);

            mvvm.getData(getUserSetting().getCountry(), type);

        });
        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adAdapter);
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> {
            if (type != null) {
                mvvm.getData(getUserSetting().getCountry(), type);

            } else {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel);
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }
}