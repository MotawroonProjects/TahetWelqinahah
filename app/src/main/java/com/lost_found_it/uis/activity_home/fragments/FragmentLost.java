package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.BrandAdapter;
import com.lost_found_it.adapter.CategoryAdapter;
import com.lost_found_it.databinding.FragmentLostBinding;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentLost extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentFoundLostMvvm mvvm;
    private FragmentLostBinding binding;
    private HomeActivity activity;
    private CategoryAdapter categoryAdapter;
    private BrandAdapter brandAdapter;
    private AdAdapter adAdapter;
    private CategoryModel selectedCategory;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String category_id;
    private String sub_category_id="0";
    private String search=null;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentLost newInstance() {
        return new FragmentLost();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lost, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentFoundLostMvvm.class);
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());

        Observable.timer(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        loadUiData();
                    }
                });
    }

    private void loadUiData() {

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCountrySuccess().observe(activity, isChanged -> {
            binding.setCountry(getUserSetting().getCountry());
            mvvm.getCategories(getUserSetting().getCountry(), "lost", "main");

        });


        binding.cardMecca.setOnClickListener(v -> {
            generalMvvm.getOnMeccaFoundLost().setValue("lost");
            generalMvvm.getMainNavigation().setValue(Tags.fragment_mecca_pos);
        });

        binding.cardTower.setOnClickListener(v -> {
            generalMvvm.getOnTowerFoundLost().setValue("lost");
            generalMvvm.getMainNavigation().setValue(Tags.fragment_tower_pos);
        });
        generalMvvm.getOnNewAdAdded().observe(activity, adModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                mvvm.getOnDataSuccess().getValue().add(0, adModel);
                if (adAdapter != null) {
                    adAdapter.notifyItemInserted(0);
                }
            }
        });

        generalMvvm.getOnAdUpdated().observe(activity, mBoolean -> {
            mvvm.getCategories(getUserSetting().getCountry(), "lost", "main");
        });
        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayoutLost.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnCategoryDataSuccess().observe(activity, list -> {
            if (list.size() > 0) {
                selectedCategory = list.get(0);
                updateSubCategoryData(selectedCategory);

            }else {
                updateSubCategoryData(null);
            }
            categoryAdapter = new CategoryAdapter(activity, this, getLang());
            binding.recViewCategoryLost.setAdapter(categoryAdapter);
            categoryAdapter.updateList(list);

        });

        mvvm.getOnDataSuccess().observe(activity, list -> {
            binding.recViewLayoutLost.tvNoData.setText(R.string.no_item);
            if (list.size() > 0) {
                binding.recViewLayoutLost.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayoutLost.tvNoData.setVisibility(View.VISIBLE);
            }
            adAdapter.updateList(list);
        });

        binding.recViewCategoryLost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategoryLost.setAdapter(categoryAdapter);

        binding.recViewBrandsLost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        brandAdapter = new BrandAdapter(activity, this, getLang());
        binding.recViewBrandsLost.setAdapter(brandAdapter);

        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewLayoutLost.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayoutLost.recView.setAdapter(adAdapter);
        binding.recViewLayoutLost.recView.setHasFixedSize(true);
        binding.recViewLayoutLost.recView.setDrawingCacheEnabled(true);
        binding.recViewLayoutLost.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayoutLost.recView.setItemViewCacheSize(20);

        binding.recViewLayoutLost.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayoutLost.swipeRefresh.setOnRefreshListener(() -> {
            mvvm.getCategories(getUserSetting().getCountry(), "lost", "main");

        });
        binding.edtSearch.requestFocus();
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                search = binding.edtSearch.getText().toString();
                mvvm.getData(getUserSetting().getCountry(), category_id, sub_category_id, "lost", "main", null, null, search);
            }
        });
        mvvm.getCategories(getUserSetting().getCountry(), "lost", "main");

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
        sub_category_id=subCategoryModel.getId();
        mvvm.getData(getUserSetting().getCountry(), category_id, subCategoryModel.getId(), "lost", "main", null, null,search);

    }

    private void updateSubCategoryData(CategoryModel categoryModel) {

        if (categoryModel==null){
            brandAdapter.updateList(new ArrayList<>());
            mvvm.getData(getUserSetting().getCountry(), null, null, "lost", "main", null, null,search);

        }else {
            if (categoryModel.getSub_categories().size() > 0) {

                List<SubCategoryModel> list = new ArrayList<>();

                for (SubCategoryModel subCategoryModel : categoryModel.getSub_categories()) {
                    subCategoryModel.setSelected(false);
                    list.add(subCategoryModel);
                }

                SubCategoryModel subCategoryModel = new SubCategoryModel();
                subCategoryModel.setId("0");
                subCategoryModel.setSelected(true);
                list.add(0, subCategoryModel);

                brandAdapter = new BrandAdapter(activity, this, getLang());
                binding.recViewBrandsLost.setAdapter(brandAdapter);
                brandAdapter.updateList(list);


                 category_id = "0";
                if (selectedCategory != null) {
                    category_id = selectedCategory.getId();
                }
                mvvm.getData(getUserSetting().getCountry(), category_id, "0", "lost", "main", null, null,search);

            } else {
                mvvm.getData(getUserSetting().getCountry(), categoryModel.getId(), null, "lost", "main", null, null,search);

                brandAdapter = new BrandAdapter(activity, this, getLang());
                binding.recViewBrandsLost.setAdapter(brandAdapter);
                brandAdapter.updateList(new ArrayList<>());
            }
        }

    }

    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel.getId());
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }
}