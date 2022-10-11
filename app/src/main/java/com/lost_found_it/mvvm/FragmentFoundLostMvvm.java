package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CategoryDataModel;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentFoundLostMvvm extends AndroidViewModel {
    private final String TAG = FragmentFoundLostMvvm.class.getName();
    public MutableLiveData<List<AdModel>> onDataSuccess;
    public MutableLiveData<List<CategoryModel>> onCategoryDataSuccess;

    public MutableLiveData<Boolean> isLoading;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentFoundLostMvvm(@NonNull Application application) {
        super(application);


    }

    public MutableLiveData<List<AdModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<List<CategoryModel>> getOnCategoryDataSuccess() {
        if (onCategoryDataSuccess == null) {
            onCategoryDataSuccess = new MutableLiveData<>();
        }
        return onCategoryDataSuccess;
    }
    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public void getCategories(String country,String type,String place_type,String lang){

        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getCategoryWithSubCategory(country, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CategoryDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<CategoryDataModel> response) {

                        if (response.isSuccessful()){

                            if (response.body()!=null){
                                List<CategoryModel> list = new ArrayList<>(response.body().getData());
                                if (list.size()>0){
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setId("0");
                                    categoryModel.setSelected(true);
                                    list.add(0,categoryModel);
                                    List<SubCategoryModel> subList = new ArrayList<>();
                                    for (CategoryModel model:list){
                                        subList.addAll(model.getSub_categories());
                                    }
                                    categoryModel.setSub_categories(subList);
                                    getData(country,"0","0",type,place_type,null,null,null);

                                }

                                getOnCategoryDataSuccess().setValue(list);

                            }

                        }else {
                            try {
                                Log.e(TAG,response.errorBody().string()+"___"+response.body().getMessage().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getIsLoading().setValue(false);

                        Log.e(TAG,e.getMessage());
                    }
                });
    }
    public void getData(String country,String category_id,String sub_category_id,String type,String place_type,String added_later,String city_id,String search){
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url)
                .getAds(country,category_id,sub_category_id,type,place_type,search,added_later,city_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<AdsDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<AdsDataModel> response) {
                        getIsLoading().setValue(false);

                        if (response.isSuccessful()){

                            if (response.body()!=null){
                                getOnDataSuccess().setValue(response.body().getData());
                            }

                        }else {
                            try {
                                Log.e(TAG,response.errorBody().string()+"___"+response.body().getMessage().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getIsLoading().setValue(false);

                        Log.e(TAG,e.getMessage());
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
