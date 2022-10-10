package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.StatusResponse;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMyAdsMvvm extends AndroidViewModel {
    private final String TAG = FragmentMyAdsMvvm.class.getName();
    public MutableLiveData<List<AdModel>> onDataSuccess;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Integer> onDelete;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentMyAdsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<AdModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Integer> getOnDelete() {
        if (onDelete == null) {
            onDelete = new MutableLiveData<>();
        }
        return onDelete;
    }

    public void getMyAds(String country, UserModel model,String lang){
        getIsLoading().setValue(true);
        Log.e("Dldldlld",model.getData().getAccess_token());
        Api.getService(Tags.base_url)
                .getMyAds("Bearer "+model.getData().getAccess_token(),country,lang)
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
                            } catch (Exception e) {
                                Log.e("kdkkd",response.code()+"");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getIsLoading().setValue(false);
                        Log.e("error",e.getMessage());
                    }
                });
    }

    public void deleteAd(String country,UserModel model, String add_id, int adapterPosition){
        Api.getService(Tags.base_url)
                .deleteAd("Bearer "+model.getData().getAccess_token(),country,add_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<StatusResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body()!=null){
                                if (response.body().getCode()==200){
                                    if (getOnDataSuccess().getValue()!=null){
                                        getOnDataSuccess().getValue().remove(adapterPosition);

                                    }
                                    getOnDelete().setValue(adapterPosition);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
