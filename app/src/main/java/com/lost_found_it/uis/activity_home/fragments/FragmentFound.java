package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.lost_found_it.adapter.BrandAdapter;
import com.lost_found_it.adapter.CategoryAdapter;
import com.lost_found_it.databinding.FragmentFoundBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.mvvm.FragmentFoundLostMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentFound extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentFoundLostMvvm mvvm;
    private FragmentFoundBinding binding;
    private HomeActivity activity;
    private CategoryAdapter categoryAdapter;
    private BrandAdapter brandAdapter;
    private AdAdapter adAdapter;
    private CategoryModel selectedCategory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentFound newInstance() {
        return new FragmentFound();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_found, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentFoundLostMvvm.class);

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCountrySuccess().observe(activity, isChanged -> {
            binding.setCountry(getUserSetting().getCountry());
            mvvm.getCategories(getUserSetting().getCountry(), "found", "main");
        });
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());

        mvvm.getIsLoading().observe(activity,isLoading->{
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnCategoryDataSuccess().observe(activity, list -> {
            if (list.size()>0){
                selectedCategory = list.get(0);
                updateSubCategoryData(selectedCategory);

            }
            categoryAdapter = new CategoryAdapter(activity,this,getLang());
            binding.recViewCategoryFound.setAdapter(categoryAdapter);
            categoryAdapter.updateList(list);

        });

        mvvm.getOnDataSuccess().observe(activity,list->{
            binding.recViewLayout.tvNoData.setText(R.string.no_item);
            if (list.size()>0){
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            }else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            }
            adAdapter.updateList(list);
        });

        binding.recViewCategoryFound.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategoryFound.setAdapter(categoryAdapter);

        binding.recViewBrandsFound.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        brandAdapter = new BrandAdapter(activity, this, getLang());
        binding.recViewBrandsFound.setAdapter(brandAdapter);

        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adAdapter);
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> {
            mvvm.getCategories(getUserSetting().getCountry(), "found", "main");

        });

        mvvm.getCategories(getUserSetting().getCountry(), "found", "main");

    }

    public void setCategoryItemSelected(CategoryModel categoryModel) {
        this.selectedCategory = categoryModel;
        updateSubCategoryData(categoryModel);
    }

    public void setSubCategoryItemSelected(SubCategoryModel subCategoryModel) {
        String category_id = "0";
        if (selectedCategory != null) {
            category_id = selectedCategory.getId();
        }
        mvvm.getData(getUserSetting().getCountry(), category_id, subCategoryModel.getId(), "found", "main");

    }

    private void updateSubCategoryData(CategoryModel categoryModel) {

        if (categoryModel.getSub_categories().size() > 0) {


            List<SubCategoryModel> list = new ArrayList<>();

            for (SubCategoryModel subCategoryModel:categoryModel.getSub_categories()){
                subCategoryModel.setSelected(false);
                list.add(subCategoryModel);
            }

            SubCategoryModel subCategoryModel = new SubCategoryModel();
            subCategoryModel.setId("0");
            subCategoryModel.setSelected(true);
            list.add(0, subCategoryModel);

            brandAdapter = new BrandAdapter(activity,this,getLang());
            binding.recViewBrandsFound.setAdapter(brandAdapter);
            brandAdapter.updateList(list);
            String category_id = "0";
            if (selectedCategory != null) {
                category_id = selectedCategory.getId();
            }
            mvvm.getData(getUserSetting().getCountry(), category_id, "0", "found", "main");

        } else {
            mvvm.getData(getUserSetting().getCountry(), categoryModel.getId(), null, "found", "main");
            brandAdapter = new BrandAdapter(activity,this,getLang());
            binding.recViewBrandsFound.setAdapter(brandAdapter);
            brandAdapter.updateList(new ArrayList<>());

        }
    }
    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel);
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

}