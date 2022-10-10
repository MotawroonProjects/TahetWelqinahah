package com.lost_found_it.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.R;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.StatusResponse;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.share.Common;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMyFavAdsMvvm extends AndroidViewModel {
    private final String TAG = FragmentMyFavAdsMvvm.class.getName();
    public MutableLiveData<List<AdModel>> onDataSuccess;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Boolean> onUnFav;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentMyFavAdsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<Boolean> getOnUnFav() {
        if (onUnFav == null) {
            onUnFav = new MutableLiveData<>();
        }
        return onUnFav;
    }

    public MutableLiveData<List<AdModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }



    public void getMyAds(String country, UserModel model,String lang){
        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getMyFavAds("Bearer "+model.getData().getAccess_token(),country,lang)
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
    public void unFav(Context context, String country, UserModel userModel, String ad_id) {
        if (userModel == null) {
            return;
        }

        Log.e("token",userModel.getData().getAccess_token()+"_____"+ad_id);

        ProgressDialog dialog = Common.createProgressDialog(context,context.getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .followUnFollow("Bearer " + userModel.getData().getAccess_token(), country, ad_id, "love")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getCode() == 200) {
                                getOnUnFav().setValue(true);
                            }


                        } else {



                            try {
                                Log.e(TAG, response.errorBody().string() + "___" + response.body().getMessage().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.e(TAG, e.getMessage());
                    }
                });
    }

}
