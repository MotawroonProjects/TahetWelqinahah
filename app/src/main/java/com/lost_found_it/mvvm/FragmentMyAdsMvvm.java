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
    public MutableLiveData<Boolean> onDelete=new MutableLiveData<>();

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

    public void getMyData( String country){
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url)
                .getMyAds("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC90YTdldHdlbGUydDdhLmFmaWZkcml2aW5nLmNvbVwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTY1Mjc4MTY5OCwibmJmIjoxNjUyNzgxNjk4LCJqdGkiOiJQQ1JnbUxkQjNTRzE0QkFHIiwic3ViIjoxLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.oBRbtEGeCEA_QjpotWrtqfeVKSevf-Ohyut2Ojz1p5Y",country)
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
                        Log.e("error",e.getMessage());
                    }
                });
    }

    public void deleteAdd(String country,String add_id){
        Api.getService(Tags.base_url).deleteAdd("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC90YTdldHdlbGUydDdhLmFmaWZkcml2aW5nLmNvbVwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTY1Mjc4MTY5OCwibmJmIjoxNjUyNzgxNjk4LCJqdGkiOiJQQ1JnbUxkQjNTRzE0QkFHIiwic3ViIjoxLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.oBRbtEGeCEA_QjpotWrtqfeVKSevf-Ohyut2Ojz1p5Y",country,add_id)
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
                                    onDelete.postValue(true);
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
